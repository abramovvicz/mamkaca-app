package com.abramovvicz.mamkaca.data.repository

import com.abramovvicz.mamkaca.data.remote.SupabaseClient
import com.abramovvicz.mamkaca.domain.model.User
import com.abramovvicz.mamkaca.domain.repository.UserRepository
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
 * Implementacja repozytorium użytkownika wykorzystująca Supabase jako źródło danych.
 */
class UserRepositoryImpl : UserRepository {

    private val client = SupabaseClient.getClient().postgrest
    private val usersTable = "users"

    // Cache do przechowywania danych użytkowników
    private val userCache = mutableMapOf<String, User>()

    /**
     * Pobiera użytkownika na podstawie ID.
     * Najpierw sprawdza cache, a jeśli nie znajdzie, pobiera z Supabase.
     */
    override suspend fun getUserById(userId: String): Flow<User?> = flow {
        validateUserId(userId)

        // Sprawdź cache
        val cachedUser = userCache[userId]
        if (cachedUser != null) {
            emit(cachedUser)
            return@flow
        }

        try {
            val user = client.from(usersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<User>()

            // Zapisz do cache
            userCache[userId] = user

            emit(user)
        } catch (e: Exception) {
            // Obsługa błędów: brak użytkownika, problem z połączeniem itp.
            throw mapException(e, "Błąd podczas pobierania użytkownika: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Zapisuje lub aktualizuje dane użytkownika.
     */
    override suspend fun saveUser(user: User): Flow<User> = flow {
        validateUser(user)

        try {
            // Aktualizacja czasu modyfikacji
            val updatedUser = user.copy(updatedAt = ZonedDateTime.now(ZoneOffset.UTC).format(
                DateTimeFormatter.ISO_OFFSET_DATE_TIME))

            // Zapisz do Supabase - upsert
            val result = client.from(usersTable)
                .upsert(updatedUser) {
                    select()
                    onConflict = "id"
                }
                .decodeSingle<User>()

            // Aktualizuj cache
            userCache[user.id] = result

            emit(result)
        } catch (e: Exception) {
            throw mapException(e, "Błąd podczas zapisywania użytkownika: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Usuwa użytkownika na podstawie ID.
     */
    override suspend fun deleteUser(userId: String) = flow<Unit> {
        validateUserId(userId)

        try {
            client.from(usersTable)
                .delete {
                    filter {eq("id", userId)}
                }

            // Usuń z cache
            userCache.remove(userId)

            emit(Unit)
        } catch (e: Exception) {
            throw mapException(e, "Błąd podczas usuwania użytkownika: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Aktualizuje datę ostatniego picia alkoholu dla użytkownika.
     */
    override suspend fun updateLastDrinkingDate(
        userId: String,
        lastDrinkingDate: String
    ): Flow<User> = flow {
        validateUserId(userId)
        validateLastDrinkingDate(lastDrinkingDate)

        try {
            val currentUser = getUserById(userId).collect { user ->
                if (user == null) {
                    throw Exception("Nie znaleziono użytkownika o ID: $userId")
                }

                // Aktualizuj datę ostatniego picia i czas modyfikacji
                val updatedUser = user.copy(
                    lastDrinkingDate = lastDrinkingDate,
                    updatedAt = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                )

                // Zapisz do Supabase
                val result = client.from(usersTable)
                    .update(
                        {
                            set("last_drinking_date", lastDrinkingDate)
                            set("updated_at", updatedUser.updatedAt)
                        }
                    ) {
                        filter { eq("id", userId) }
                    }
                    .decodeSingle<User>()

                // Aktualizuj cache
                userCache[userId] = result

                emit(result)
            }
        } catch (e: Exception) {
            throw mapException(e, "Błąd podczas aktualizacji daty ostatniego picia: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Synchronizuje dane użytkownika z serwerem.
     */
    override suspend fun synchronizeUser(userId: String): Flow<User> = flow {
        validateUserId(userId)

        try {
            // Pobierz aktualną wersję z serwera
            val serverUser = client.from(usersTable)
                .select(columns = Columns.ALL) {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingleOrNull<User>()

            if (serverUser == null) {
                throw Exception("Nie znaleziono użytkownika o ID: $userId na serwerze")
            }

            // Aktualizuj cache
            userCache[userId] = serverUser

            emit(serverUser)
        } catch (e: Exception) {
            throw mapException(e, "Błąd podczas synchronizacji użytkownika: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Sprawdza, czy dane użytkownika są dostępne lokalnie.
     */
    override suspend fun isUserCached(userId: String): Boolean {
        validateUserId(userId)
        return userCache.containsKey(userId)
    }

    /**
     * Czyści cache użytkownika.
     */
    override suspend fun clearUserCache(userId: String) {
        validateUserId(userId)
        userCache.remove(userId)
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
     * Waliduje obiekt użytkownika.
     */
    private fun validateUser(user: User) {
        if (user.id.isBlank()) {
            throw IllegalArgumentException("ID użytkownika nie może być puste")
        }
    }

    /**
     * Waliduje datę ostatniego picia alkoholu.
     */
    private fun validateLastDrinkingDate(lastDrinkingDate: String) {
        if (lastDrinkingDate.isBlank()) {
            throw IllegalArgumentException("Data ostatniego picia nie może być pusta")
        }

        // Można dodać dodatkową walidację formatu daty
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
