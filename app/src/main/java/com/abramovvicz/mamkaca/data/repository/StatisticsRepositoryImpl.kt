package com.abramovvicz.mamkaca.data.repository

import com.abramovvicz.mamkaca.data.remote.SupabaseClient
import com.abramovvicz.mamkaca.domain.model.Answer
import com.abramovvicz.mamkaca.domain.model.AnswerType
import com.abramovvicz.mamkaca.domain.repository.StatisticsRepository
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Implementacja repozytorium statystyk wykorzystująca Supabase jako źródło danych.
 */
class StatisticsRepositoryImpl : StatisticsRepository {

    private val client = SupabaseClient.getClient().postgrest
    private val answersTable = "answers"

    // Cache dla statystyk użytkownika
    private val statisticsCache = mutableMapOf<String, Map<String, Any>>()
    
    /**
     * Pobiera liczbę odpowiedzi "tak" dla danego użytkownika w podanym okresie.
     */
    override suspend fun getYesAnswersCount(
        userId: String,
        startDate: String,
        endDate: String
    ): Flow<Int> = flow {
        validateUserId(userId)
        validateDate(startDate)
        validateDate(endDate)

        // Klucz cache
        val cacheKey = "yes_count_${userId}_${startDate}_${endDate}"
        
        // Sprawdź cache
        val cachedCount = statisticsCache[userId]?.get(cacheKey) as? Int
        if (cachedCount != null) {
            emit(cachedCount)
            return@flow
        }

        try {
            val answers = client.from(answersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        gte("answer_date", startDate)
                        lte("answer_date", endDate)
                        eq("answer_type", "yes")
                    }
                }
                .decodeList<Answer>()

            val count = answers.size

            // Aktualizuj cache
            updateStatisticsCache(userId, cacheKey, count)

            emit(count)
        } catch (e: Exception) {
            throw mapException(
                e,
                "Błąd podczas pobierania liczby odpowiedzi 'tak': ${e.message}"
            )
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Pobiera liczbę odpowiedzi "nie" dla danego użytkownika w podanym okresie.
     */
    override suspend fun getNoAnswersCount(
        userId: String,
        startDate: String,
        endDate: String
    ): Flow<Int> = flow {
        validateUserId(userId)
        validateDate(startDate)
        validateDate(endDate)

        // Klucz cache
        val cacheKey = "no_count_${userId}_${startDate}_${endDate}"
        
        // Sprawdź cache
        val cachedCount = statisticsCache[userId]?.get(cacheKey) as? Int
        if (cachedCount != null) {
            emit(cachedCount)
            return@flow
        }

        try {
            val answers = client.from(answersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        gte("answer_date", startDate)
                        lte("answer_date", endDate)
                        eq("answer_type", "no")
                    }
                }
                .decodeList<Answer>()

            val count = answers.size

            // Aktualizuj cache
            updateStatisticsCache(userId, cacheKey, count)

            emit(count)
        } catch (e: Exception) {
            throw mapException(
                e,
                "Błąd podczas pobierania liczby odpowiedzi 'nie': ${e.message}"
            )
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Pobiera liczbę dni z rzędu bez kaca dla danego użytkownika.
     */
    override suspend fun getConsecutiveNoDaysCount(userId: String): Flow<Int> = flow {
        validateUserId(userId)

        // Klucz cache
        val cacheKey = "consecutive_no_days_${userId}"
        
        // Sprawdź cache
        val cachedCount = statisticsCache[userId]?.get(cacheKey) as? Int
        if (cachedCount != null) {
            emit(cachedCount)
            return@flow
        }

        try {
            // Pobierz wszystkie odpowiedzi użytkownika posortowane malejąco według daty
            val allAnswers = client.from(answersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                    }
                    order("answer_date", Order.DESCENDING)
                }
                .decodeList<Answer>()

            // Grupuj odpowiedzi według daty (bierzemy najnowszą odpowiedź z danego dnia)
            val answersPerDay = allAnswers.groupBy { it.answerDate }
                .mapValues { (_, answers) -> answers.maxByOrNull { it.answerTime } }
                .filterValues { it != null }
                .mapValues { it.value!! }

            // Oblicz aktualną serię dni bez kaca
            var consecutiveDays = 0
            val today = LocalDate.now()
            var currentDate = today
            
            while (true) {
                val dateStr = currentDate.toString()
                val answer = answersPerDay[dateStr]
                
                if (answer == null) {
                    // Jeśli brak odpowiedzi na dany dzień, przerwij liczenie
                    break
                }
                
                if (answer.answerType == AnswerType.YES) {
                    // Jeśli użytkownik miał kaca, przerwij liczenie
                    break
                }
                
                // Zwiększ licznik dni bez kaca
                consecutiveDays++
                
                // Przejdź do poprzedniego dnia
                currentDate = currentDate.minusDays(1)
            }

            // Aktualizuj cache
            updateStatisticsCache(userId, cacheKey, consecutiveDays)

            emit(consecutiveDays)
        } catch (e: Exception) {
            throw mapException(
                e,
                "Błąd podczas obliczania liczby dni z rzędu bez kaca: ${e.message}"
            )
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Pobiera najdłuższy okres bez kaca dla danego użytkownika.
     */
    override suspend fun getLongestStreakWithoutHangover(userId: String): Flow<Int> = flow {
        validateUserId(userId)

        // Klucz cache
        val cacheKey = "longest_streak_${userId}"
        
        // Sprawdź cache
        val cachedStreak = statisticsCache[userId]?.get(cacheKey) as? Int
        if (cachedStreak != null) {
            emit(cachedStreak)
            return@flow
        }

        try {
            // Pobierz wszystkie odpowiedzi użytkownika posortowane rosnąco według daty
            val allAnswers = client.from(answersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                    }
                    order("answer_date", Order.ASCENDING)
                }
                .decodeList<Answer>()

            // Grupuj odpowiedzi według daty (bierzemy odpowiedź z największą wartością answerType)
            val answersPerDay = allAnswers.groupBy { it.answerDate }
                .mapValues { (_, answers) -> 
                    // W przypadku wielu odpowiedzi w jednym dniu, jeśli którakolwiek to "YES",
                    // to cały dzień liczymy jako dzień z kacem
                    answers.any { it.answerType == AnswerType.YES }
                }

            // Oblicz najdłuższy okres bez kaca
            var currentStreak = 0
            var longestStreak = 0
            
            // Sortujemy daty, aby przejść przez odpowiedzi chronologicznie
            val sortedDates = answersPerDay.keys.sorted()
            
            for (i in sortedDates.indices) {
                val date = sortedDates[i]
                val hadHangover = answersPerDay[date] ?: false
                
                if (!hadHangover) {
                    // Jeśli nie miał kaca, zwiększ bieżącą serię
                    currentStreak++
                    
                    // Sprawdź, czy to najdłuższa seria
                    if (currentStreak > longestStreak) {
                        longestStreak = currentStreak
                    }
                } else {
                    // Jeśli miał kaca, zresetuj bieżącą serię
                    currentStreak = 0
                }
                
                // Sprawdź, czy między bieżącą datą a następną jest przerwa
                if (i < sortedDates.size - 1) {
                    val currentDate = LocalDate.parse(date)
                    val nextDate = LocalDate.parse(sortedDates[i + 1])
                    val daysBetween = ChronoUnit.DAYS.between(currentDate, nextDate)
                    
                    // Jeśli przerwa wynosi więcej niż 1 dzień, zresetuj bieżącą serię
                    if (daysBetween > 1) {
                        currentStreak = 0
                    }
                }
            }

            // Aktualizuj cache
            updateStatisticsCache(userId, cacheKey, longestStreak)

            emit(longestStreak)
        } catch (e: Exception) {
            throw mapException(
                e,
                "Błąd podczas obliczania najdłuższego okresu bez kaca: ${e.message}"
            )
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Pobiera procent dni, w których użytkownik miał kaca, w podanym okresie.
     */
    override suspend fun getHangoverPercentage(
        userId: String,
        startDate: String,
        endDate: String
    ): Flow<Float> = flow {
        validateUserId(userId)
        validateDate(startDate)
        validateDate(endDate)

        // Klucz cache
        val cacheKey = "hangover_percentage_${userId}_${startDate}_${endDate}"
        
        // Sprawdź cache
        val cachedPercentage = statisticsCache[userId]?.get(cacheKey) as? Float
        if (cachedPercentage != null) {
            emit(cachedPercentage)
            return@flow
        }

        try {
            // Pobierz wszystkie odpowiedzi użytkownika w danym okresie
            val allAnswers = client.from(answersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        gte("answer_date", startDate)
                        lte("answer_date", endDate)
                    }
                }
                .decodeList<Answer>()

            // Grupuj odpowiedzi według daty
            val answersPerDay = allAnswers.groupBy { it.answerDate }
                .mapValues { (_, answers) -> 
                    // Jeśli którakolwiek odpowiedź w danym dniu to "YES", to cały dzień to dzień z kacem
                    answers.any { it.answerType == AnswerType.YES }
                }

            val totalDays = LocalDate.parse(startDate).until(
                LocalDate.parse(endDate).plusDays(1),
                ChronoUnit.DAYS
            ).toInt()
            
            val daysWithAnswers = answersPerDay.size
            val daysWithHangover = answersPerDay.values.count { it }
            
            // Oblicz procent dni z kacem, uwzględniając tylko dni z odpowiedziami
            val percentage = if (daysWithAnswers > 0) {
                (daysWithHangover.toFloat() / daysWithAnswers) * 100
            } else {
                0f
            }

            // Aktualizuj cache
            updateStatisticsCache(userId, cacheKey, percentage)

            emit(percentage)
        } catch (e: Exception) {
            throw mapException(
                e,
                "Błąd podczas obliczania procentu dni z kacem: ${e.message}"
            )
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Pobiera rozkład odpowiedzi w dni tygodnia dla danego użytkownika.
     */
    override suspend fun getWeekdayDistribution(
        userId: String,
        startDate: String,
        endDate: String
    ): Flow<Map<Int, Int>> = flow {
        validateUserId(userId)
        validateDate(startDate)
        validateDate(endDate)

        // Klucz cache
        val cacheKey = "weekday_distribution_${userId}_${startDate}_${endDate}"
        
        // Sprawdź cache
        val cachedDistribution = statisticsCache[userId]?.get(cacheKey) as? Map<Int, Int>
        if (cachedDistribution != null) {
            emit(cachedDistribution)
            return@flow
        }

        try {
            // Pobierz odpowiedzi "tak" użytkownika w danym okresie
            val yesAnswers = client.from(answersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        gte("answer_date", startDate)
                        lte("answer_date", endDate)
                        eq("answer_type", "yes")
                    }
                }
                .decodeList<Answer>()

            // Inicjalizuj mapę dni tygodnia (1 = poniedziałek, 7 = niedziela)
            val distribution = (1..7).associateWith { 0 }.toMutableMap()
            
            // Dla każdej odpowiedzi, określ dzień tygodnia i zwiększ licznik
            yesAnswers.forEach { answer ->
                val date = LocalDate.parse(answer.answerDate)
                val dayOfWeek = date.dayOfWeek.value
                distribution[dayOfWeek] = distribution[dayOfWeek]!! + 1
            }

            // Aktualizuj cache
            updateStatisticsCache(userId, cacheKey, distribution)

            emit(distribution)
        } catch (e: Exception) {
            throw mapException(
                e,
                "Błąd podczas pobierania rozkładu dni tygodnia: ${e.message}"
            )
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Pobiera miesięczną statystykę odpowiedzi dla danego użytkownika.
     */
    override suspend fun getMonthlyStatistics(userId: String, year: Int): Flow<Map<Int, Int>> = flow {
        validateUserId(userId)
        validateYear(year)

        // Klucz cache
        val cacheKey = "monthly_statistics_${userId}_${year}"
        
        // Sprawdź cache
        val cachedStatistics = statisticsCache[userId]?.get(cacheKey) as? Map<Int, Int>
        if (cachedStatistics != null) {
            emit(cachedStatistics)
            return@flow
        }

        try {
            val startDate = LocalDate.of(year, 1, 1)
            val endDate = LocalDate.of(year, 12, 31)
            
            // Pobierz odpowiedzi "tak" użytkownika w danym roku
            val yesAnswers = client.from(answersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        gte("answer_date", startDate.toString())
                        lte("answer_date", endDate.toString())
                        eq("answer_type", "yes")
                    }
                }
                .decodeList<Answer>()

            // Inicjalizuj mapę miesięcy (1 = styczeń, 12 = grudzień)
            val monthlyStats = (1..12).associateWith { 0 }.toMutableMap()
            
            // Dla każdej odpowiedzi, określ miesiąc i zwiększ licznik
            yesAnswers.forEach { answer ->
                val date = LocalDate.parse(answer.answerDate)
                val month = date.monthValue
                monthlyStats[month] = monthlyStats[month]!! + 1
            }

            // Aktualizuj cache
            updateStatisticsCache(userId, cacheKey, monthlyStats)

            emit(monthlyStats)
        } catch (e: Exception) {
            throw mapException(
                e,
                "Błąd podczas pobierania statystyk miesięcznych: ${e.message}"
            )
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Pobiera datę ostatniego dnia, w którym użytkownik miał kaca.
     */
    override suspend fun getLastHangoverDate(userId: String): Flow<String?> = flow {
        validateUserId(userId)

        // Klucz cache
        val cacheKey = "last_hangover_date_${userId}"
        
        // Sprawdź cache
        val cachedDate = statisticsCache[userId]?.get(cacheKey) as? String
        if (cachedDate != null) {
            emit(cachedDate)
            return@flow
        }

        try {
            // Pobierz najnowszą odpowiedź "tak" użytkownika
            val lastHangoverAnswer = client.from(answersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        eq("answer_type", "yes")
                    }
                    order("answer_date", Order.DESCENDING)
                    limit(1)
                }
                .decodeList<Answer>()
                .firstOrNull()

            val lastHangoverDate = lastHangoverAnswer?.answerDate

            // Aktualizuj cache
            if (lastHangoverDate != null) {
                updateStatisticsCache(userId, cacheKey, lastHangoverDate)
            }

            emit(lastHangoverDate)
        } catch (e: Exception) {
            throw mapException(
                e,
                "Błąd podczas pobierania daty ostatniego kaca: ${e.message}"
            )
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Synchronizuje dane statystyczne z serwerem.
     * W przypadku tego repozytorium synchronizacja polega na wyczyszczeniu cache,
     * ponieważ dane są zawsze pobierane na żywo z Supabase.
     */
    override suspend fun synchronizeStatistics(userId: String): Flow<Unit> = flow {
        validateUserId(userId)

        try {
            // Wyczyść cache dla danego użytkownika, co wymusi ponowne pobranie danych
            clearStatisticsCache(userId)
            emit(Unit)
        } catch (e: Exception) {
            throw mapException(
                e,
                "Błąd podczas synchronizacji danych statystycznych: ${e.message}"
            )
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Czy dane statystyczne są dostępne lokalnie.
     */
    override suspend fun areStatisticsCached(userId: String): Boolean {
        validateUserId(userId)
        return statisticsCache.containsKey(userId) && statisticsCache[userId]?.isNotEmpty() == true
    }

    /**
     * Czyści cache danych statystycznych.
     */
    override suspend fun clearStatisticsCache(userId: String) {
        validateUserId(userId)
        statisticsCache.remove(userId)
    }

    /**
     * Aktualizuje cache statystyk dla danego użytkownika.
     */
    private fun updateStatisticsCache(userId: String, key: String, value: Any) {
        val userStats = statisticsCache[userId]?.toMutableMap() ?: mutableMapOf()
        userStats[key] = value
        statisticsCache[userId] = userStats
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
     * Waliduje datę.
     */
    private fun validateDate(date: String) {
        if (date.isBlank()) {
            throw IllegalArgumentException("Data nie może być pusta")
        }

        try {
            LocalDate.parse(date)
        } catch (e: Exception) {
            throw IllegalArgumentException("Nieprawidłowy format daty: $date")
        }
    }

    /**
     * Waliduje rok.
     */
    private fun validateYear(year: Int) {
        val currentYear = LocalDate.now().year
        if (year < 2000 || year > currentYear) {
            throw IllegalArgumentException("Rok musi być pomiędzy 2000 a $currentYear")
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
