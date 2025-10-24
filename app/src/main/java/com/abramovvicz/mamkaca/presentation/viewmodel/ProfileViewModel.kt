package com.abramovvicz.mamkaca.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abramovvicz.mamkaca.domain.model.User
import com.abramovvicz.mamkaca.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * ViewModel zarządzający ekranem profilu użytkownika.
 */
class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    // Publiczny obserwowany stan UI
    private val _uiState = MutableStateFlow<ProfileUIState>(ProfileUIState.Loading)
    val uiState: StateFlow<ProfileUIState> = _uiState
    
    // Stan ustawień użytkownika
    private val _settingsState = MutableStateFlow(UserSettingsState())
    val settingsState: StateFlow<UserSettingsState> = _settingsState
    
    // Aktualny użytkownik
    private var currentUser: User? = null
    
    // Stan logowania
    private var isLoggedIn = false

    init {
        checkLoginState()
    }

    /**
     * Sprawdza stan logowania użytkownika
     */
    private fun checkLoginState() {
        _uiState.value = ProfileUIState.Loading
        
        // W rzeczywistej implementacji sprawdzanie logowania
        // odbywałoby się przez serwis autentykacji
        isLoggedIn = false
        
        // Jeśli użytkownik jest zalogowany, pobieramy jego dane
        if (isLoggedIn && currentUser?.id != null) {
            loadUserProfile(currentUser!!.id)
        } else {
            _uiState.value = ProfileUIState.Unauthenticated
        }
    }

    /**
     * Obsługuje intencje z UI
     */
    fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.Login -> login(intent.email, intent.password)
            is ProfileIntent.SignUp -> signUp(intent.email, intent.password)
            is ProfileIntent.SignOut -> signOut()
            is ProfileIntent.UpdateProfile -> updateProfile(intent.user)
            is ProfileIntent.UpdateLastDrinkingDate -> updateLastDrinkingDate(intent.date)
            is ProfileIntent.UpdateNotificationSettings -> updateNotificationSettings(intent.enabled)
            is ProfileIntent.UpdateSecuritySettings -> updateSecuritySettings(intent.pinEnabled, intent.pin)
        }
    }

    /**
     * Loguje użytkownika
     */
    private fun login(email: String, password: String) {
        _uiState.value = ProfileUIState.Loading
        
        // W rzeczywistej implementacji logowanie odbywałoby się przez serwis autentykacji
        // Tutaj symulujemy udane logowanie
        val userId = "user_123"
        
        viewModelScope.launch {
            try {
                userRepository.getUserById(userId)
                    .catch { e ->
                        _uiState.value = ProfileUIState.Error("Błąd podczas logowania: ${e.message}")
                    }
                    .collect { user ->
                        if (user != null) {
                            currentUser = user
                            isLoggedIn = true
                            _uiState.value = ProfileUIState.Authenticated(user)
                            loadUserSettings()
                        } else {
                            _uiState.value = ProfileUIState.Error("Nie znaleziono użytkownika")
                        }
                    }
            } catch (e: Exception) {
                _uiState.value = ProfileUIState.Error("Błąd podczas logowania: ${e.message}")
            }
        }
    }

    /**
     * Rejestruje nowego użytkownika
     */
    private fun signUp(email: String, password: String) {
        _uiState.value = ProfileUIState.Loading
        
        // W rzeczywistej implementacji rejestracja odbywałaby się przez serwis autentykacji
        // Tutaj symulujemy udaną rejestrację
        val userId = "new_user_" + System.currentTimeMillis()
        
        viewModelScope.launch {
            try {
                // Tworzymy nowego użytkownika
                val newUser = User.createNew(userId, email)
                
                // Zapisujemy użytkownika
                userRepository.saveUser(newUser)
                    .catch { e ->
                        _uiState.value = ProfileUIState.Error("Błąd podczas rejestracji: ${e.message}")
                    }
                    .collect { savedUser ->
                        currentUser = savedUser
                        isLoggedIn = true
                        _uiState.value = ProfileUIState.Authenticated(savedUser)
                        
                        // Inicjalizacja ustawień użytkownika
                        _settingsState.value = UserSettingsState()
                    }
            } catch (e: Exception) {
                _uiState.value = ProfileUIState.Error("Błąd podczas rejestracji: ${e.message}")
            }
        }
    }

    /**
     * Wylogowuje użytkownika
     */
    private fun signOut() {
        _uiState.value = ProfileUIState.Loading
        
        // W rzeczywistej implementacji wylogowanie odbywałoby się przez serwis autentykacji
        currentUser = null
        isLoggedIn = false
        _uiState.value = ProfileUIState.Unauthenticated
        _settingsState.value = UserSettingsState()
    }

    /**
     * Pobiera profil użytkownika
     */
    private fun loadUserProfile(userId: String) {
        _uiState.value = ProfileUIState.Loading
        
        viewModelScope.launch {
            try {
                userRepository.getUserById(userId)
                    .catch { e ->
                        _uiState.value = ProfileUIState.Error("Błąd podczas pobierania profilu: ${e.message}")
                    }
                    .collect { user ->
                        if (user != null) {
                            currentUser = user
                            _uiState.value = ProfileUIState.Authenticated(user)
                            loadUserSettings()
                        } else {
                            _uiState.value = ProfileUIState.Unauthenticated
                        }
                    }
            } catch (e: Exception) {
                _uiState.value = ProfileUIState.Error("Błąd podczas pobierania profilu: ${e.message}")
            }
        }
    }

    /**
     * Aktualizuje profil użytkownika
     */
    private fun updateProfile(updatedUser: User) {
        if (!isLoggedIn || currentUser == null) {
            _uiState.value = ProfileUIState.Error("Użytkownik nie jest zalogowany")
            return
        }

        _uiState.value = ProfileUIState.Loading
        
        viewModelScope.launch {
            try {
                // Aktualizujemy użytkownika, zachowując ID
                val userToUpdate = currentUser!!.copy(
                    email = updatedUser.email,
                    timezone = updatedUser.timezone,
                    updatedAt = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                )
                
                userRepository.saveUser(userToUpdate)
                    .catch { e ->
                        _uiState.value = ProfileUIState.Error("Błąd podczas aktualizacji profilu: ${e.message}")
                    }
                    .collect { savedUser ->
                        currentUser = savedUser
                        _uiState.value = ProfileUIState.Authenticated(savedUser)
                    }
            } catch (e: Exception) {
                _uiState.value = ProfileUIState.Error("Błąd podczas aktualizacji profilu: ${e.message}")
            }
        }
    }

    /**
     * Aktualizuje datę ostatniego picia
     */
    private fun updateLastDrinkingDate(date: String) {
        if (!isLoggedIn || currentUser == null) {
            _uiState.value = ProfileUIState.Error("Użytkownik nie jest zalogowany")
            return
        }

        _uiState.value = ProfileUIState.Loading
        
        viewModelScope.launch {
            try {
                userRepository.updateLastDrinkingDate(currentUser!!.id, date)
                    .catch { e ->
                        _uiState.value = ProfileUIState.Error("Błąd podczas aktualizacji daty ostatniego picia: ${e.message}")
                    }
                    .collect { updatedUser ->
                        currentUser = updatedUser
                        _uiState.value = ProfileUIState.Authenticated(updatedUser)
                    }
            } catch (e: Exception) {
                _uiState.value = ProfileUIState.Error("Błąd podczas aktualizacji daty ostatniego picia: ${e.message}")
            }
        }
    }

    /**
     * Pobiera ustawienia użytkownika
     */
    private fun loadUserSettings() {
        // W rzeczywistej implementacji ustawienia byłyby pobierane z bazy danych
        // Tutaj symulujemy domyślne ustawienia
        _settingsState.value = UserSettingsState(
            notificationsEnabled = true,
            securityPinEnabled = false,
            securityPin = null
        )
    }

    /**
     * Aktualizuje ustawienia powiadomień
     */
    private fun updateNotificationSettings(enabled: Boolean) {
        _settingsState.value = _settingsState.value.copy(
            notificationsEnabled = enabled
        )
        
        // W rzeczywistej implementacji ustawienia byłyby zapisywane w bazie danych
    }

    /**
     * Aktualizuje ustawienia zabezpieczeń
     */
    private fun updateSecuritySettings(pinEnabled: Boolean, pin: String?) {
        _settingsState.value = _settingsState.value.copy(
            securityPinEnabled = pinEnabled,
            securityPin = pin
        )
        
        // W rzeczywistej implementacji ustawienia byłyby zapisywane w bazie danych
    }
}

/**
 * Stany UI dla ekranu profilu
 */
sealed class ProfileUIState {
    // Stan ładowania danych
    object Loading : ProfileUIState()
    
    // Stan użytkownika niezalogowanego
    object Unauthenticated : ProfileUIState()
    
    // Stan użytkownika zalogowanego
    data class Authenticated(val user: User) : ProfileUIState()
    
    // Stan błędu
    data class Error(val message: String) : ProfileUIState()
}

/**
 * Stan ustawień użytkownika
 */
data class UserSettingsState(
    val notificationsEnabled: Boolean = false,
    val securityPinEnabled: Boolean = false,
    val securityPin: String? = null
)

/**
 * Intencje dla ekranu profilu
 */
sealed class ProfileIntent {
    // Intencja logowania
    data class Login(val email: String, val password: String) : ProfileIntent()
    
    // Intencja rejestracji
    data class SignUp(val email: String, val password: String) : ProfileIntent()
    
    // Intencja wylogowania
    object SignOut : ProfileIntent()
    
    // Intencja aktualizacji profilu
    data class UpdateProfile(val user: User) : ProfileIntent()
    
    // Intencja aktualizacji daty ostatniego picia
    data class UpdateLastDrinkingDate(val date: String) : ProfileIntent()
    
    // Intencja aktualizacji ustawień powiadomień
    data class UpdateNotificationSettings(val enabled: Boolean) : ProfileIntent()
    
    // Intencja aktualizacji ustawień zabezpieczeń
    data class UpdateSecuritySettings(val pinEnabled: Boolean, val pin: String?) : ProfileIntent()
}
