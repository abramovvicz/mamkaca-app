package com.abramovvicz.mamkaca.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abramovvicz.mamkaca.domain.model.Answer
import com.abramovvicz.mamkaca.domain.model.AnswerType
import com.abramovvicz.mamkaca.domain.repository.AnswerRepository
import com.abramovvicz.mamkaca.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * ViewModel zarządzający ekranem statystyk odpowiedzi użytkownika.
 */
class StatsViewModel(
    private val answerRepository: AnswerRepository,
    private val statisticsRepository: StatisticsRepository
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
            is StatsIntent.SetUserId -> setUserId(intent.userId)
            is StatsIntent.SetDeviceId -> setDeviceId(intent.deviceId)
            is StatsIntent.LoadMonthlyStats -> loadMonthlyStats(intent.year)
            is StatsIntent.LoadWeekdayDistribution -> loadWeekdayDistribution()
        }
    }

    /**
     * Ustawia ID użytkownika do analizy statystyk
     */
    private fun setUserId(newUserId: String?) {
        userId = newUserId
        loadStats()
    }

    /**
     * Ustawia ID urządzenia do analizy statystyk
     */
    private fun setDeviceId(newDeviceId: String) {
        deviceId = newDeviceId
        loadStats()
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
                    // Pobranie wszystkich potrzebnych statystyk równolegle
                    val yesAnswersCount = statisticsRepository.getYesAnswersCount(userId!!, startDate, endDate)
                        .catch { e ->
                            _uiState.value = StatsUIState.Error("Błąd podczas pobierania liczby odpowiedzi 'tak': ${e.message}")
                            return@catch
                        }
                    
                    val noAnswersCount = statisticsRepository.getNoAnswersCount(userId!!, startDate, endDate)
                        .catch { e ->
                            _uiState.value = StatsUIState.Error("Błąd podczas pobierania liczby odpowiedzi 'nie': ${e.message}")
                            return@catch
                        }
                    
                    val hangoverPercentage = statisticsRepository.getHangoverPercentage(userId!!, startDate, endDate)
                        .catch { e ->
                            _uiState.value = StatsUIState.Error("Błąd podczas obliczania procentu dni z kacem: ${e.message}")
                            return@catch
                        }
                    
                    val longestStreak = statisticsRepository.getLongestStreakWithoutHangover(userId!!)
                        .catch { e ->
                            _uiState.value = StatsUIState.Error("Błąd podczas obliczania najdłuższej serii bez kaca: ${e.message}")
                            return@catch
                        }
                    
                    val currentStreak = statisticsRepository.getConsecutiveNoDaysCount(userId!!)
                        .catch { e ->
                            _uiState.value = StatsUIState.Error("Błąd podczas obliczania aktualnej serii bez kaca: ${e.message}")
                            return@catch
                        }
                    
                    val lastHangoverDate = statisticsRepository.getLastHangoverDate(userId!!)
                        .catch { e ->
                            _uiState.value = StatsUIState.Error("Błąd podczas pobierania daty ostatniego kaca: ${e.message}")
                            return@catch
                        }
                    
                    // Zbieramy wszystkie wyniki i aktualizujemy UI
                    yesAnswersCount.collect { yesCount ->
                        noAnswersCount.collect { noCount ->
                            hangoverPercentage.collect { percentage ->
                                longestStreak.collect { longest ->
                                    currentStreak.collect { current ->
                                        lastHangoverDate.collect { lastDate ->
                                            _uiState.value = StatsUIState.StatsLoaded(
                                                totalAnswers = yesCount + noCount,
                                                yesAnswers = yesCount,
                                                noAnswers = noCount,
                                                percentageYes = percentage,
                                                longestStreakDays = longest,
                                                currentStreakDays = current,
                                                periodDays = periodDays,
                                                lastHangoverDate = lastDate
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Jeśli nie ma userId, używamy starego sposobu na podstawie urządzenia
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
                periodDays = periodDays,
                lastHangoverDate = null
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
        
        // Znajdź datę ostatniego kaca
        val lastHangoverAnswer = sortedAnswers.findLast { it.answerType == AnswerType.YES }
        val lastHangoverDate = lastHangoverAnswer?.answerDate

        _uiState.value = StatsUIState.StatsLoaded(
            totalAnswers = totalAnswers,
            yesAnswers = yesAnswers,
            noAnswers = noAnswers,
            percentageYes = percentageYes,
            longestStreakDays = longestStreak,
            currentStreakDays = currentStreakDays,
            periodDays = periodDays,
            lastHangoverDate = lastHangoverDate
        )
    }
    
    /**
     * Pobiera miesięczne statystyki dla danego roku
     */
    private fun loadMonthlyStats(year: Int) {
        _uiState.value = StatsUIState.Loading
        
        viewModelScope.launch {
            try {
                if (userId != null) {
                    statisticsRepository.getMonthlyStatistics(userId!!, year)
                        .catch { e ->
                            _uiState.value = StatsUIState.Error("Błąd podczas pobierania statystyk miesięcznych: ${e.message}")
                        }
                        .collect { monthlyStats ->
                            _uiState.value = StatsUIState.MonthlyStatsLoaded(
                                yearStats = year,
                                monthlyData = monthlyStats.map { (month, count) ->
                                    MonthStat(
                                        month = month,
                                        monthName = getMonthName(month),
                                        hangoverCount = count
                                    )
                                }.sortedBy { it.month }
                            )
                        }
                } else {
                    // Dla urządzenia, musimy zrobić to na podstawie odpowiedzi
                    val startDate = LocalDate.of(year, 1, 1).toString()
                    val endDate = LocalDate.of(year, 12, 31).toString()
                    
                    answerRepository.getAnswersByDeviceId(deviceId)
                        .catch { e ->
                            _uiState.value = StatsUIState.Error("Błąd podczas pobierania statystyk miesięcznych: ${e.message}")
                        }
                        .collect { answers ->
                            val filteredAnswers = answers.filter {
                                val answerDate = LocalDate.parse(it.answerDate)
                                answerDate.year == year && it.answerType == AnswerType.YES
                            }
                            
                            // Grupuj odpowiedzi według miesiąca
                            val monthlyData = (1..12).associateWith { month ->
                                filteredAnswers.count {
                                    LocalDate.parse(it.answerDate).monthValue == month
                                }
                            }
                            
                            _uiState.value = StatsUIState.MonthlyStatsLoaded(
                                yearStats = year,
                                monthlyData = monthlyData.map { (month, count) ->
                                    MonthStat(
                                        month = month,
                                        monthName = getMonthName(month),
                                        hangoverCount = count
                                    )
                                }.sortedBy { it.month }
                            )
                        }
                }
            } catch (e: Exception) {
                _uiState.value = StatsUIState.Error("Błąd podczas pobierania statystyk miesięcznych: ${e.message}")
            }
        }
    }
    
    /**
     * Pobiera rozkład odpowiedzi w dni tygodnia
     */
    private fun loadWeekdayDistribution() {
        _uiState.value = StatsUIState.Loading
        
        val endDate = LocalDate.now().toString()
        val startDate = LocalDate.now().minusDays(periodDays.toLong()).toString()
        
        viewModelScope.launch {
            try {
                if (userId != null) {
                    statisticsRepository.getWeekdayDistribution(userId!!, startDate, endDate)
                        .catch { e ->
                            _uiState.value = StatsUIState.Error("Błąd podczas pobierania rozkładu dni tygodnia: ${e.message}")
                        }
                        .collect { distribution ->
                            _uiState.value = StatsUIState.WeekdayDistributionLoaded(
                                weekdayData = distribution.map { (day, count) ->
                                    WeekdayStat(
                                        dayOfWeek = day,
                                        dayName = getDayName(day),
                                        hangoverCount = count
                                    )
                                }.sortedBy { it.dayOfWeek }
                            )
                        }
                } else {
                    // Dla urządzenia, musimy zrobić to na podstawie odpowiedzi
                    answerRepository.getAnswersByDeviceId(deviceId)
                        .catch { e ->
                            _uiState.value = StatsUIState.Error("Błąd podczas pobierania rozkładu dni tygodnia: ${e.message}")
                        }
                        .collect { answers ->
                            val filteredAnswers = answers.filter {
                                it.answerDate >= startDate && 
                                it.answerDate <= endDate && 
                                it.answerType == AnswerType.YES
                            }
                            
                            // Grupuj odpowiedzi według dnia tygodnia
                            val weekdayData = (1..7).associateWith { day ->
                                filteredAnswers.count {
                                    LocalDate.parse(it.answerDate).dayOfWeek.value == day
                                }
                            }
                            
                            _uiState.value = StatsUIState.WeekdayDistributionLoaded(
                                weekdayData = weekdayData.map { (day, count) ->
                                    WeekdayStat(
                                        dayOfWeek = day,
                                        dayName = getDayName(day),
                                        hangoverCount = count
                                    )
                                }.sortedBy { it.dayOfWeek }
                            )
                        }
                }
            } catch (e: Exception) {
                _uiState.value = StatsUIState.Error("Błąd podczas pobierania rozkładu dni tygodnia: ${e.message}")
            }
        }
    }
    
    /**
     * Zwraca nazwę miesiąca dla danego numeru
     */
    private fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "Styczeń"
            2 -> "Luty"
            3 -> "Marzec"
            4 -> "Kwiecień"
            5 -> "Maj"
            6 -> "Czerwiec"
            7 -> "Lipiec"
            8 -> "Sierpień"
            9 -> "Wrzesień"
            10 -> "Październik"
            11 -> "Listopad"
            12 -> "Grudzień"
            else -> "Nieznany"
        }
    }
    
    /**
     * Zwraca nazwę dnia tygodnia dla danego numeru
     */
    private fun getDayName(day: Int): String {
        return when (day) {
            1 -> "Poniedziałek"
            2 -> "Wtorek"
            3 -> "Środa"
            4 -> "Czwartek"
            5 -> "Piątek"
            6 -> "Sobota"
            7 -> "Niedziela"
            else -> "Nieznany"
        }
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
        val periodDays: Int,
        val lastHangoverDate: String?
    ) : StatsUIState()
    
    // Stan po załadowaniu statystyk miesięcznych
    data class MonthlyStatsLoaded(
        val yearStats: Int,
        val monthlyData: List<MonthStat>
    ) : StatsUIState()
    
    // Stan po załadowaniu statystyk dni tygodnia
    data class WeekdayDistributionLoaded(
        val weekdayData: List<WeekdayStat>
    ) : StatsUIState()
    
    // Stan błędu
    data class Error(val message: String) : StatsUIState()
}

/**
 * Statystyka dla miesiąca
 */
data class MonthStat(
    val month: Int,
    val monthName: String,
    val hangoverCount: Int
)

/**
 * Statystyka dla dnia tygodnia
 */
data class WeekdayStat(
    val dayOfWeek: Int,
    val dayName: String,
    val hangoverCount: Int
)

/**
 * Intencje dla ekranu statystyk
 */
sealed class StatsIntent {
    // Intencja pobrania statystyk
    object LoadStats : StatsIntent()
    
    // Intencja filtrowania statystyk według okresu (w dniach)
    data class FilterByPeriod(val days: Int) : StatsIntent()
    
    // Intencja ustawienia ID użytkownika
    data class SetUserId(val userId: String?) : StatsIntent()
    
    // Intencja ustawienia ID urządzenia
    data class SetDeviceId(val deviceId: String) : StatsIntent()
    
    // Intencja pobrania statystyk miesięcznych
    data class LoadMonthlyStats(val year: Int) : StatsIntent()
    
    // Intencja pobrania rozkładu dni tygodnia
    object LoadWeekdayDistribution : StatsIntent()
}
