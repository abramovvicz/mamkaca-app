package com.abramovvicz.mamkaca.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abramovvicz.mamkaca.domain.model.Answer
import com.abramovvicz.mamkaca.domain.model.AnswerSource
import com.abramovvicz.mamkaca.domain.model.AnswerType
import com.abramovvicz.mamkaca.domain.repository.AnswerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * ViewModel zarządzający ekranem głównym z pytaniem "Masz dziś kaca?"
 */
class HomeViewModel(
    private val answerRepository: AnswerRepository
) : ViewModel() {

    // Publiczny obserwowany stan UI
    private val _uiState = MutableStateFlow<HomeUIState>(HomeUIState.Loading)
    val uiState: StateFlow<HomeUIState> = _uiState

    // Obecny stan formularza
    private val _formState = MutableStateFlow(AnswerFormState())
    val formState: StateFlow<AnswerFormState> = _formState

    // User ID - w rzeczywistej aplikacji byłoby pobierane z repozytorium użytkownika
    private var userId: String? = null
    
    // Device ID - w rzeczywistej aplikacji byłoby pobierane z systemu
    private var deviceId: String = "00000000-0000-4000-8000-000000000101"

    init {
        loadTodayAnswer()
    }

    /**
     * Pobiera odpowiedź z dzisiejszego dnia, jeśli istnieje
     */
    fun loadTodayAnswer() {
        _uiState.value = HomeUIState.Loading
        
        val today = LocalDate.now().toString()
        
        viewModelScope.launch {
            try {
                val answers = if (userId != null) {
                    answerRepository.getAnswersByUserIdAndDate(userId!!, today)
                        .catch { e -> 
                            _uiState.value = HomeUIState.Error("Błąd podczas pobierania odpowiedzi: ${e.message}")
                        }
                        .collect { answers ->
                            processAnswers(answers, today)
                        }
                } else {
                    answerRepository.getAnswersByDeviceId(deviceId)
                        .catch { e ->
                            _uiState.value = HomeUIState.Error("Błąd podczas pobierania odpowiedzi: ${e.message}")
                        }
                        .collect { answers ->
                            processAnswers(answers.filter { it.answerDate == today }, today)
                        }
                }
            } catch (e: Exception) {
                _uiState.value = HomeUIState.Error("Błąd podczas pobierania odpowiedzi: ${e.message}")
            }
        }
    }

    /**
     * Przetwarza pobrane odpowiedzi
     */
    private fun processAnswers(answers: List<Answer>, today: String) {
        if (answers.isNotEmpty()) {
            // Jeśli mamy odpowiedź z dzisiaj, aktualizujemy stan
            val todayAnswer = answers.first()
            _uiState.value = HomeUIState.AnswerSaved(
                answerType = todayAnswer.answerType,
                note = todayAnswer.note,
                savedAt = todayAnswer.answerTime
            )
        } else {
            // Jeśli nie ma odpowiedzi z dzisiaj, pokazujemy ekran z pytaniem
            _uiState.value = HomeUIState.ReadyToAnswer
        }
    }

    /**
     * Aktualizuje notatkę w formularzu
     */
    fun updateNote(note: String) {
        _formState.value = _formState.value.copy(note = note)
    }

    /**
     * Obsługuje intencje z UI
     */
    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SubmitAnswer -> submitAnswer(intent.answerType)
            is HomeIntent.ResetAnswer -> resetAnswer()
            is HomeIntent.UpdateNote -> updateNote(intent.note)
        }
    }

    /**
     * Przesyła odpowiedź do zapisania
     */
    private fun submitAnswer(answerType: AnswerType) {
        _uiState.value = HomeUIState.Loading
        
        viewModelScope.launch {
            try {
                val answer = Answer.create(
                    userId = userId,
                    deviceId = deviceId,
                    answerType = answerType,
                    note = _formState.value.note.takeIf { it.isNotBlank() },
                    answerSource = AnswerSource.MANUAL
                )

                answerRepository.saveAnswer(answer)
                    .catch { e ->
                        _uiState.value = HomeUIState.Error("Błąd podczas zapisywania odpowiedzi: ${e.message}")
                    }
                    .collect { savedAnswer ->
                        _uiState.value = HomeUIState.AnswerSaved(
                            answerType = savedAnswer.answerType,
                            note = savedAnswer.note,
                            savedAt = savedAnswer.answerTime
                        )
                        _formState.value = AnswerFormState() // Resetujemy formularz
                    }
            } catch (e: Exception) {
                _uiState.value = HomeUIState.Error("Błąd podczas zapisywania odpowiedzi: ${e.message}")
            }
        }
    }

    /**
     * Resetuje odpowiedź (tylko do celów demonstracyjnych)
     */
    private fun resetAnswer() {
        _uiState.value = HomeUIState.ReadyToAnswer
        _formState.value = AnswerFormState()
    }
}

/**
 * Stany UI dla ekranu głównego
 */
sealed class HomeUIState {
    // Stan ładowania danych
    object Loading : HomeUIState()
    
    // Stan gotowości do odpowiedzi (brak odpowiedzi z dzisiaj)
    object ReadyToAnswer : HomeUIState()
    
    // Stan po zapisaniu odpowiedzi
    data class AnswerSaved(
        val answerType: AnswerType,
        val note: String? = null,
        val savedAt: String
    ) : HomeUIState()
    
    // Stan błędu
    data class Error(val message: String) : HomeUIState()
}

/**
 * Stan formularza odpowiedzi
 */
data class AnswerFormState(
    val note: String = ""
)

/**
 * Intencje dla ekranu głównego
 */
sealed class HomeIntent {
    // Intencja przesłania odpowiedzi
    data class SubmitAnswer(val answerType: AnswerType) : HomeIntent()
    
    // Intencja aktualizacji notatki
    data class UpdateNote(val note: String) : HomeIntent()
    
    // Intencja zresetowania odpowiedzi (tylko do celów demonstracyjnych)
    object ResetAnswer : HomeIntent()
}
