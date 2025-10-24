package com.abramovvicz.mamkaca.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abramovvicz.mamkaca.domain.model.Answer
import com.abramovvicz.mamkaca.domain.model.AnswerType
import com.abramovvicz.mamkaca.domain.repository.AnswerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * ViewModel zarządzający ekranem statystyk odpowiedzi użytkownika.
 */
class StatsViewModel(
    private val answerRepository: AnswerRepository
) : ViewModel() {

    // Publiczny obserwowany stan UI
    private val _uiState = MutableStateFlow<StatsUIState>(StatsUIState.Loading)
    val uiState: StateFlow<StatsUIState> = _uiState

    // User ID - w rzeczywistej aplikacji byłoby pobierane z repozytorium użytkownika
    private var userId: String? = null
    
    // Device ID - w rzeczywistej aplikacji byłoby pobierane z systemu
    private var deviceId: String = "test_device_id"
    
    // Domyślny okres analizy statystyk
    private var periodDays: Int = 30

    init {
        loadStats()
    }

    /**
     * Obsługuje intencje z UI
     */
    fun handleIntent(intent: StatsIntent) {
        when (intent) {
            is StatsIntent.LoadStats -> loadStats()
            is StatsIntent.FilterByPeriod -> filterByPeriod(intent.days)
        }
    }

    /**
     * Pobiera statystyki odpowiedzi użytkownika
     */
    private fun loadStats() {
        _uiState.value = StatsUIState.Loading
        
        val endDate = LocalDate.now().toString()
        val startDate = LocalDate.now().minusDays(periodDays.toLong()).toString()
        
        viewModelScope.launch {
            try {
                if (userId != null) {
                    answerRepository.getAnswersByPeriod(userId!!, startDate, endDate)
                        .catch { e ->
                            _uiState.value = StatsUIState.Error("Błąd podczas pobierania statystyk: ${e.message}")
                        }
                        .collect { answers ->
                            processAnswers(answers, startDate, endDate)
                        }
                } else {
                    answerRepository.getAnswersByDeviceId(deviceId)
                        .catch { e ->
                            _uiState.value = StatsUIState.Error("Błąd podczas pobierania statystyk: ${e.message}")
                        }
                        .collect { answers ->
                            val filteredAnswers = answers.filter {
                                it.answerDate >= startDate && it.answerDate <= endDate
                            }
                            processAnswers(filteredAnswers, startDate, endDate)
                        }
                }
            } catch (e: Exception) {
                _uiState.value = StatsUIState.Error("Błąd podczas pobierania statystyk: ${e.message}")
            }
        }
    }

    /**
     * Filtruje statystyki według wybranego okresu
     */
    private fun filterByPeriod(days: Int) {
        periodDays = days
        loadStats()
    }

    /**
     * Przetwarza pobrane odpowiedzi na statystyki
     */
    private fun processAnswers(answers: List<Answer>, startDate: String, endDate: String) {
        if (answers.isEmpty()) {
            _uiState.value = StatsUIState.StatsLoaded(
                totalAnswers = 0,
                yesAnswers = 0,
                noAnswers = 0,
                percentageYes = 0f,
                longestStreakDays = 0,
                currentStreakDays = 0,
                periodDays = periodDays
            )
            return
        }

        // Oblicz podstawowe statystyki
        val totalAnswers = answers.size
        val yesAnswers = answers.count { it.answerType == AnswerType.YES }
        val noAnswers = answers.count { it.answerType == AnswerType.NO }
        val percentageYes = if (totalAnswers > 0) {
            (yesAnswers.toFloat() / totalAnswers) * 100f
        } else {
            0f
        }

        // Obliczanie najdłuższej serii dni bez kaca (AnswerType.NO)
        val sortedAnswers = answers.sortedBy { it.answerDate }
        var currentStreak = 0
        var longestStreak = 0
        var lastDate: LocalDate? = null
        
        sortedAnswers.forEach { answer ->
            val currentDate = LocalDate.parse(answer.answerDate)
            
            if (answer.answerType == AnswerType.NO) {
                // Jeśli to pierwszy dzień lub poprzedni dzień był bezpośrednio przed obecnym
                if (lastDate == null || ChronoUnit.DAYS.between(lastDate, currentDate) == 1L) {
                    currentStreak++
                } else {
                    // Przerwa w serii
                    currentStreak = 1
                }
                
                // Aktualizacja najdłuższej serii
                longestStreak = maxOf(longestStreak, currentStreak)
            } else {
                // Przerwanie serii
                currentStreak = 0
            }
            
            lastDate = currentDate
        }

        // Aktualna seria dni bez kaca
        var currentStreakDays = 0
        val today = LocalDate.now()
        
        // Sprawdzenie, czy ostatnia odpowiedź jest typu NO i jest z ostatniego dnia
        if (sortedAnswers.lastOrNull()?.answerType == AnswerType.NO) {
            val lastAnswerDate = LocalDate.parse(sortedAnswers.last().answerDate)
            
            // Jeśli ostatnia odpowiedź jest z dzisiaj lub wczoraj (aktywna seria)
            if (ChronoUnit.DAYS.between(lastAnswerDate, today) <= 1) {
                // Znajdź początek aktualnej serii
                var currentDate = lastAnswerDate
                currentStreakDays = 1 // Zaczynamy od 1 dla ostatniej odpowiedzi
                
                // Idź wstecz w czasie, sprawdzając czy poprzednie dni również miały odpowiedzi NO
                for (i in 1..sortedAnswers.size) {
                    val previousDay = currentDate.minusDays(1)
                    val previousDayAnswer = sortedAnswers.findLast { it.answerDate == previousDay.toString() }
                    
                    if (previousDayAnswer != null && previousDayAnswer.answerType == AnswerType.NO) {
                        currentStreakDays++
                        currentDate = previousDay
                    } else {
                        break
                    }
                }
            }
        }

        _uiState.value = StatsUIState.StatsLoaded(
            totalAnswers = totalAnswers,
            yesAnswers = yesAnswers,
            noAnswers = noAnswers,
            percentageYes = percentageYes,
            longestStreakDays = longestStreak,
            currentStreakDays = currentStreakDays,
            periodDays = periodDays
        )
    }
}

/**
 * Stany UI dla ekranu statystyk
 */
sealed class StatsUIState {
    // Stan ładowania danych
    object Loading : StatsUIState()
    
    // Stan po załadowaniu statystyk
    data class StatsLoaded(
        val totalAnswers: Int,
        val yesAnswers: Int,
        val noAnswers: Int,
        val percentageYes: Float,
        val longestStreakDays: Int,
        val currentStreakDays: Int,
        val periodDays: Int
    ) : StatsUIState()
    
    // Stan błędu
    data class Error(val message: String) : StatsUIState()
}

/**
 * Intencje dla ekranu statystyk
 */
sealed class StatsIntent {
    // Intencja pobrania statystyk
    object LoadStats : StatsIntent()
    
    // Intencja filtrowania statystyk według okresu (w dniach)
    data class FilterByPeriod(val days: Int) : StatsIntent()
}
