package com.abramovvicz.mamkaca.domain.repository

import com.abramovvicz.mamkaca.domain.model.Answer
import com.abramovvicz.mamkaca.domain.model.AnswerType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repozytorium odpowiedzialne za operacje na danych odpowiedzi użytkownika.
 */
interface AnswerRepository {
    /**
     * Pobiera odpowiedź na podstawie ID.
     * @param answerId ID odpowiedzi do pobrania.
     * @return Flow zawierający obiekt Answer lub null jeśli nie znaleziono.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun getAnswerById(answerId: String): Flow<Answer?>
    
    /**
     * Pobiera wszystkie odpowiedzi dla danego użytkownika.
     * @param userId ID użytkownika, dla którego pobierane są odpowiedzi.
     * @return Flow zawierający listę obiektów Answer.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun getAnswersByUserId(userId: String): Flow<List<Answer>>
    
    /**
     * Pobiera odpowiedzi dla danego użytkownika z określonego dnia.
     * @param userId ID użytkownika, dla którego pobierane są odpowiedzi.
     * @param date Data w formacie String, z której pobierane są odpowiedzi.
     * @return Flow zawierający listę obiektów Answer.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun getAnswersByUserIdAndDate(userId: String, date: String): Flow<List<Answer>>
    
    /**
     * Zapisuje lub aktualizuje odpowiedź.
     * @param answer Obiekt Answer do zapisania lub aktualizacji.
     * @return Flow zawierający zaktualizowany obiekt Answer.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun saveAnswer(answer: Answer): Flow<Answer>
    
    /**
     * Usuwa odpowiedź na podstawie ID.
     * @param answerId ID odpowiedzi do usunięcia.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun deleteAnswer(answerId: String): Flow<Unit>

    /**
     * Pobiera odpowiedzi dla danego urządzenia.
     * @param deviceId ID urządzenia, dla którego pobierane są odpowiedzi.
     * @return Flow zawierający listę obiektów Answer.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun getAnswersByDeviceId(deviceId: String): Flow<List<Answer>>
    
    /**
     * Pobiera odpowiedzi dla danego okresu.
     * @param userId ID użytkownika, dla którego pobierane są odpowiedzi.
     * @param startDate Data początkowa okresu w formacie String.
     * @param endDate Data końcowa okresu w formacie String.
     * @return Flow zawierający listę obiektów Answer.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun getAnswersByPeriod(userId: String, startDate: String, endDate: String): Flow<List<Answer>>
    
    /**
     * Synchronizuje odpowiedzi z serwerem.
     * @param userId ID użytkownika, którego odpowiedzi są synchronizowane.
     * @return Flow zawierający listę synchronizowanych obiektów Answer.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun synchronizeAnswers(userId: String): Flow<List<Answer>>
    
    /**
     * Czy odpowiedzi są dostępne lokalnie.
     * @param userId ID użytkownika, którego odpowiedzi są sprawdzane.
     * @return true jeśli odpowiedzi są dostępne lokalnie, false w przeciwnym przypadku.
     */
    suspend fun areAnswersCached(userId: String): Boolean
    
    /**
     * Czyści cache odpowiedzi użytkownika.
     * @param userId ID użytkownika, którego cache odpowiedzi ma być wyczyszczone.
     */
    suspend fun clearAnswersCache(userId: String)
}
