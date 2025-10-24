package com.abramovvicz.mamkaca.domain.repository

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Repozytorium odpowiedzialne za operacje na danych statystycznych.
 */
interface StatisticsRepository {
    /**
     * Pobiera liczbę odpowiedzi "tak" dla danego użytkownika w podanym okresie.
     * @param userId ID użytkownika, dla którego pobierane są statystyki.
     * @param startDate Data początkowa okresu w formacie String.
     * @param endDate Data końcowa okresu w formacie String.
     * @return Flow zawierający liczbę odpowiedzi "tak".
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun getYesAnswersCount(userId: String, startDate: String, endDate: String): Flow<Int>
    
    /**
     * Pobiera liczbę odpowiedzi "nie" dla danego użytkownika w podanym okresie.
     * @param userId ID użytkownika, dla którego pobierane są statystyki.
     * @param startDate Data początkowa okresu w formacie String.
     * @param endDate Data końcowa okresu w formacie String.
     * @return Flow zawierający liczbę odpowiedzi "nie".
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun getNoAnswersCount(userId: String, startDate: String, endDate: String): Flow<Int>
    
    /**
     * Pobiera liczbę dni z rzędu bez kaca dla danego użytkownika.
     * @param userId ID użytkownika, dla którego pobierane są statystyki.
     * @return Flow zawierający liczbę dni z rzędu bez kaca.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun getConsecutiveNoDaysCount(userId: String): Flow<Int>
    
    /**
     * Pobiera najdłuższy okres bez kaca dla danego użytkownika.
     * @param userId ID użytkownika, dla którego pobierane są statystyki.
     * @return Flow zawierający liczbę dni najdłuższego okresu bez kaca.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun getLongestStreakWithoutHangover(userId: String): Flow<Int>
    
    /**
     * Pobiera procent dni, w których użytkownik miał kaca, w podanym okresie.
     * @param userId ID użytkownika, dla którego pobierane są statystyki.
     * @param startDate Data początkowa okresu w formacie String.
     * @param endDate Data końcowa okresu w formacie String.
     * @return Flow zawierający procent dni z kacem (0-100).
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun getHangoverPercentage(userId: String, startDate: String, endDate: String): Flow<Float>
    
    /**
     * Pobiera rozkład odpowiedzi w dni tygodnia dla danego użytkownika.
     * @param userId ID użytkownika, dla którego pobierane są statystyki.
     * @param startDate Data początkowa okresu w formacie String.
     * @param endDate Data końcowa okresu w formacie String.
     * @return Flow zawierający mapę dni tygodnia i liczby odpowiedzi "tak".
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun getWeekdayDistribution(userId: String, startDate: String, endDate: String): Flow<Map<Int, Int>>
    
    /**
     * Pobiera miesięczną statystykę odpowiedzi dla danego użytkownika.
     * @param userId ID użytkownika, dla którego pobierane są statystyki.
     * @param year Rok, dla którego pobierane są statystyki.
     * @return Flow zawierający mapę miesięcy i liczby odpowiedzi "tak".
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun getMonthlyStatistics(userId: String, year: Int): Flow<Map<Int, Int>>
    
    /**
     * Pobiera datę ostatniego dnia, w którym użytkownik miał kaca.
     * @param userId ID użytkownika, dla którego pobierane są statystyki.
     * @return Flow zawierający datę ostatniego dnia z kacem lub null jeśli nie znaleziono.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun getLastHangoverDate(userId: String): Flow<String?>
    
    /**
     * Synchronizuje dane statystyczne z serwerem.
     * @param userId ID użytkownika, dla którego synchronizowane są dane.
     * @return Flow informujący o zakończeniu synchronizacji.
     * @throws Exception w przypadku błędu komunikacji lub innych problemów.
     */
    suspend fun synchronizeStatistics(userId: String): Flow<Unit>
    
    /**
     * Czy dane statystyczne są dostępne lokalnie.
     * @param userId ID użytkownika, dla którego sprawdzane są dane.
     * @return true jeśli dane są dostępne lokalnie, false w przeciwnym przypadku.
     */
    suspend fun areStatisticsCached(userId: String): Boolean
    
    /**
     * Czyści cache danych statystycznych.
     * @param userId ID użytkownika, którego cache danych ma być wyczyszczone.
     */
    suspend fun clearStatisticsCache(userId: String)
}
