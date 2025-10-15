package com.abramovvicz.mamkaca.domain.repository

import com.abramovvicz.mamkaca.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repozytorium odpowiedzialne za operacje na danych użytkownika.
 */
interface UserRepository {
    /**
     * Pobiera użytkownika na podstawie ID.
     * @param userId ID użytkownika do pobrania.
     * @return Flow zawierający obiekt User lub null jeśli nie znaleziono.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun getUserById(userId: String): Flow<User?>
    
    /**
     * Zapisuje lub aktualizuje dane użytkownika.
     * @param user Obiekt User do zapisania lub aktualizacji.
     * @return Flow zawierający zaktualizowany obiekt User.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun saveUser(user: User): Flow<User>
    
    /**
     * Usuwa użytkownika na podstawie ID.
     * @param userId ID użytkownika do usunięcia.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun deleteUser(userId: String): Flow<Unit>

    /**
     * Aktualizuje datę ostatniego picia alkoholu dla użytkownika.
     * @param userId ID użytkownika do zaktualizowania.
     * @param lastDrinkingDate Data ostatniego picia w formacie String.
     * @return Flow zawierający zaktualizowany obiekt User.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun updateLastDrinkingDate(userId: String, lastDrinkingDate: String): Flow<User>
    
    /**
     * Synchronizuje dane użytkownika z serwerem.
     * @param userId ID użytkownika do synchronizacji.
     * @return Flow zawierający synchronizowany obiekt User.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun synchronizeUser(userId: String): Flow<User>
    
    /**
     * Czy dane użytkownika są dostępne lokalnie.
     * @param userId ID użytkownika do sprawdzenia.
     * @return true jeśli dane są dostępne lokalnie, false w przeciwnym przypadku.
     */
    suspend fun isUserCached(userId: String): Boolean
    
    /**
     * Czyści cache użytkownika.
     * @param userId ID użytkownika którego cache ma być wyczyszczone.
     */
    suspend fun clearUserCache(userId: String)
}
