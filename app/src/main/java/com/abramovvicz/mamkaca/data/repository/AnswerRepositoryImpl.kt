package com.abramovvicz.mamkaca.data.repository

import android.util.Log
import com.abramovvicz.mamkaca.data.remote.SupabaseClient
import com.abramovvicz.mamkaca.domain.model.Answer
import com.abramovvicz.mamkaca.domain.repository.AnswerRepository
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


/**
 * Implementacja repozytorium odpowiedzi wykorzystująca Supabase jako źródło danych.
 */
class AnswerRepositoryImpl : AnswerRepository {

    private val client = SupabaseClient.getClient().postgrest
    private val answersTable = "answers"

    // Cache do przechowywania odpowiedzi
    private val answersCache = mutableMapOf<String, Answer>()
    private val userAnswersCache = mutableMapOf<String, List<Answer>>()
    private val deviceAnswersCache = mutableMapOf<String, List<Answer>>()

    /**
     * Pobiera odpowiedź na podstawie ID.
     * Najpierw sprawdza cache, a jeśli nie znajdzie, pobiera z Supabase.
     */
    override suspend fun getAnswerById(answerId: String): Flow<Answer?> = flow {
        validateAnswerId(answerId)

        // Sprawdź cache
        val cachedAnswer = answersCache[answerId]
        if (cachedAnswer != null) {
            emit(cachedAnswer)
            return@flow
        }

        try {
            val answer = client.from(answersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("id", answerId)
                    }
                }
                .decodeSingle<Answer>()

            // Zapisz do cache
            answersCache[answerId] = answer

            emit(answer)
        } catch (e: Exception) {
            // Obsługa błędów: brak odpowiedzi, problem z połączeniem itp.
            throw mapException(e, "Błąd podczas pobierania odpowiedzi: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Pobiera wszystkie odpowiedzi dla danego użytkownika.
     */
    override suspend fun getAnswersByUserId(userId: String): Flow<List<Answer>> = flow {
        validateUserId(userId)

        // Sprawdź cache
        val cachedAnswers = userAnswersCache[userId]
        if (cachedAnswers != null) {
            emit(cachedAnswers)
            return@flow
        }

        try {
            val answers = client.from(answersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                    }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<Answer>()

            // Zapisz do cache
            userAnswersCache[userId] = answers
            answers.forEach { answersCache[it.id] = it }

            emit(answers)
        } catch (e: Exception) {
            throw mapException(e, "Błąd podczas pobierania odpowiedzi użytkownika: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Pobiera odpowiedzi dla danego użytkownika z określonego dnia.
     */
    override suspend fun getAnswersByUserIdAndDate(
        userId: String,
        date: String
    ): Flow<List<Answer>> = flow {
        validateUserId(userId)
        validateDate(date)

        try {
            val answers = client.from(answersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        eq("answer_date", date)
                    }
                    order("answer_time", Order.ASCENDING)
                }
                .decodeList<Answer>()


            // Aktualizacja cache
            val userAnswers = userAnswersCache[userId]?.toMutableList() ?: mutableListOf()
            val filteredAnswers = userAnswers.filterNot { it.answerDate == date }
            userAnswersCache[userId] = filteredAnswers.plus(answers)
            answers.forEach { answersCache[it.id] = it }

            emit(answers)
        } catch (e: Exception) {
            throw mapException(e, "Błąd podczas pobierania odpowiedzi z dnia $date: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Zapisuje lub aktualizuje odpowiedź.
     */
    override suspend fun saveAnswer(answer: Answer): Flow<Answer> = flow {
        validateAnswer(answer)

        try {
            // Aktualizacja czasu modyfikacji
            val updatedAnswer = answer.copy(updatedAt = ZonedDateTime.now(ZoneOffset.UTC).format(
                DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            val result = client.from(answersTable)
                .upsert(updatedAnswer) {
                    select()
                    onConflict = "id"
                }
                .decodeSingle<Answer>()

            // Aktualizuj cache
            answersCache[result.id] = result

            // Aktualizuj cache odpowiedzi użytkownika
            if (result.userId != null) {
                val userAnswers =
                    userAnswersCache[result.userId]?.toMutableList() ?: mutableListOf()
                val filteredAnswers = userAnswers.filterNot { it.id == result.id }
                userAnswersCache[result.userId] = filteredAnswers.plus(result)
            }

            // Aktualizuj cache odpowiedzi urządzenia
            if (result.deviceId != null) {
                val deviceAnswers =
                    deviceAnswersCache[result.deviceId]?.toMutableList() ?: mutableListOf()
                val filteredAnswers = deviceAnswers.filterNot { it.id == result.id }
                deviceAnswersCache[result.deviceId] = filteredAnswers.plus(result)
            }

            emit(result)
        } catch (e: Exception) {
            Log.e("e {}", "${e.message}");
            throw mapException(e, "Błąd podczas zapisywania odpowiedzi: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Usuwa odpowiedź na podstawie ID.
     */
    override suspend fun deleteAnswer(answerId: String) = flow<Unit> {
        validateAnswerId(answerId)

        try {
            // Pobierz odpowiedź przed usunięciem, aby zaktualizować cache
            val answerToDelete = answersCache[answerId] ?: run {
                client.from(answersTable)
                    .select(columns = Columns.ALL) {
                        filter {
                            eq("id", answerId)
                        }
                    }
                    .decodeSingleOrNull<Answer>()
            }

            client.from(answersTable)
                .delete {
                    filter {
                        eq("id", answerId)
                    }
                }

            // Usuń z cache
            answersCache.remove(answerId)

            // Aktualizuj pozostałe cache
            if (answerToDelete != null) {
                if (answerToDelete.userId != null) {
                    val userAnswers =
                        userAnswersCache[answerToDelete.userId]?.toMutableList() ?: mutableListOf()
                    userAnswersCache[answerToDelete.userId] =
                        userAnswers.filterNot { it.id == answerId }
                }

                if (answerToDelete.deviceId != null) {
                    val deviceAnswers = deviceAnswersCache[answerToDelete.deviceId]?.toMutableList()
                        ?: mutableListOf()
                    deviceAnswersCache[answerToDelete.deviceId] =
                        deviceAnswers.filterNot { it.id == answerId }
                }
            }

            emit(Unit)
        } catch (e: Exception) {
            throw mapException(e, "Błąd podczas usuwania odpowiedzi: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Pobiera odpowiedzi dla danego urządzenia.
     */
    override suspend fun getAnswersByDeviceId(deviceId: String): Flow<List<Answer>> = flow {
        validateDeviceId(deviceId)

        // Sprawdź cache
        val cachedAnswers = deviceAnswersCache[deviceId]
        if (cachedAnswers != null) {
            emit(cachedAnswers)
            return@flow
        }

        try {
            val answers = client.from(answersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("device_id", deviceId)
                    }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<Answer>()

            // Zapisz do cache
            deviceAnswersCache[deviceId] = answers
            answers.forEach { answersCache[it.id] = it }

            emit(answers)
        } catch (e: Exception) {
            throw mapException(e, "Błąd podczas pobierania odpowiedzi dla urządzenia: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Pobiera odpowiedzi dla danego okresu.
     */
    override suspend fun getAnswersByPeriod(
        userId: String,
        startDate: String,
        endDate: String
    ): Flow<List<Answer>> = flow {
        validateUserId(userId)
        validateDate(startDate)
        validateDate(endDate)

        try {
            val answers = client.from(answersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        gte("answer_date", startDate)
                        lte("answer_date", endDate)
                    }
                    order("answer_date", Order.ASCENDING)
                    order("answer_time", Order.ASCENDING)
                }
                .decodeList<Answer>()

            // Aktualizacja cache - te odpowiedzi są zbyt specyficzne, żeby je cachować
            // w standardowym userAnswersCache, więc po prostu aktualizujemy pojedyncze odpowiedzi
            answers.forEach { answersCache[it.id] = it }

            emit(answers)
        } catch (e: Exception) {
            throw mapException(
                e,
                "Błąd podczas pobierania odpowiedzi z okresu $startDate - $endDate: ${e.message}"
            )
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Synchronizuje odpowiedzi z serwerem.
     */
    override suspend fun synchronizeAnswers(userId: String): Flow<List<Answer>> = flow {
        validateUserId(userId)

        try {
            // Pobierz aktualną wersję z serwera
            val serverAnswers = client.from(answersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                    }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<Answer>()

            // Aktualizuj cache
            userAnswersCache[userId] = serverAnswers
            serverAnswers.forEach { answersCache[it.id] = it }

            emit(serverAnswers)
        } catch (e: Exception) {
            throw mapException(e, "Błąd podczas synchronizacji odpowiedzi: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Czy odpowiedzi są dostępne lokalnie.
     */
    override suspend fun areAnswersCached(userId: String): Boolean {
        validateUserId(userId)
        return userAnswersCache.containsKey(userId) && userAnswersCache[userId]?.isNotEmpty() == true
    }

    /**
     * Czyści cache odpowiedzi użytkownika.
     */
    override suspend fun clearAnswersCache(userId: String) {
        validateUserId(userId)

        // Pobierz odpowiedzi użytkownika
        val userAnswers = userAnswersCache[userId]

        // Usuń z głównego cache
        userAnswers?.forEach { answersCache.remove(it.id) }

        // Usuń z cache użytkownika
        userAnswersCache.remove(userId)
    }

    /**
     * Waliduje ID odpowiedzi.
     */
    private fun validateAnswerId(answerId: String) {
        if (answerId.isBlank()) {
            throw IllegalArgumentException("ID odpowiedzi nie może być puste")
        }
    }

    /**
     * Waliduje ID użytkownika.
     */
    private fun validateUserId(userId: String) {
        if (userId.isBlank()) {
            throw IllegalArgumentException("ID użytkownika nie może być puste")
        }
    }

    /**
     * Waliduje ID urządzenia.
     */
    private fun validateDeviceId(deviceId: String) {
        if (deviceId.isBlank()) {
            throw IllegalArgumentException("ID urządzenia nie może być puste")
        }
    }

    /**
     * Waliduje datę.
     */
    private fun validateDate(date: String) {
        if (date.isBlank()) {
            throw IllegalArgumentException("Data nie może być pusta")
        }

        // Można dodać dodatkową walidację formatu daty
    }

    /**
     * Waliduje obiekt odpowiedzi.
     */
    private fun validateAnswer(answer: Answer) {
        if (answer.id.isBlank()) {
            throw IllegalArgumentException("ID odpowiedzi nie może być puste")
        }

        if (answer.answerDate.isBlank()) {
            throw IllegalArgumentException("Data odpowiedzi nie może być pusta")
        }

        if (answer.answerTime.isBlank()) {
            throw IllegalArgumentException("Czas odpowiedzi nie może być pusty")
        }

        if (answer.userId.isNullOrBlank() && answer.deviceId.isNullOrBlank()) {
            throw IllegalArgumentException("Przynajmniej jedno z ID użytkownika lub ID urządzenia musi być podane")
        }
    }

    /**
     * Mapuje wyjątki na bardziej czytelne komunikaty.
     */
    private fun mapException(e: Exception, message: String): Exception {
        return when (e) {
            is IllegalArgumentException -> e
            else -> Exception(message, e)
        }
    }
}
