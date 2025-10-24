package com.abramovvicz.mamkaca.presentation.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abramovvicz.mamkaca.domain.model.User
import com.abramovvicz.mamkaca.presentation.viewmodel.ProfileIntent
import com.abramovvicz.mamkaca.presentation.viewmodel.ProfileUIState
import com.abramovvicz.mamkaca.presentation.viewmodel.ProfileViewModel
import com.abramovvicz.mamkaca.presentation.viewmodel.UserSettingsState
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

/**
 * Ekran profilu użytkownika
 */
@Composable
fun ProfileScreen() {
    // Pobieramy ViewModel za pomocą Koin
    val viewModel: ProfileViewModel = koinViewModel()
    
    // Obserwujemy stan UI
    val uiState by viewModel.uiState.collectAsState()
    
    // Obserwujemy stan ustawień
    val settingsState by viewModel.settingsState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val state = uiState) {
            is ProfileUIState.Loading -> {
                // Ekran ładowania
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is ProfileUIState.Unauthenticated -> {
                // Ekran dla niezalogowanego użytkownika
                AnonymousProfileScreen(
                    onLogin = { email, password ->
                        viewModel.handleIntent(ProfileIntent.Login(email, password))
                    },
                    onSignUp = { email, password ->
                        viewModel.handleIntent(ProfileIntent.SignUp(email, password))
                    }
                )
            }
            
            is ProfileUIState.Authenticated -> {
                // Ekran dla zalogowanego użytkownika
                UserProfileScreen(
                    user = state.user,
                    settingsState = settingsState,
                    onSignOut = {
                        viewModel.handleIntent(ProfileIntent.SignOut)
                    },
                    onUpdateLastDrinkingDate = { date ->
                        viewModel.handleIntent(ProfileIntent.UpdateLastDrinkingDate(date))
                    },
                    onUpdateNotificationSettings = { enabled ->
                        viewModel.handleIntent(ProfileIntent.UpdateNotificationSettings(enabled))
                    },
                    onUpdateSecuritySettings = { pinEnabled, pin ->
                        viewModel.handleIntent(ProfileIntent.UpdateSecuritySettings(pinEnabled, pin))
                    }
                )
            }
            
            is ProfileUIState.Error -> {
                // Ekran z błędem
                ErrorScreen(
                    message = state.message,
                    onRetry = {
                        // W rzeczywistej aplikacji potrzebowalibyśmy więcej kontekstu
                        // by wiedzieć, jaką akcję ponowić
                        viewModel.handleIntent(ProfileIntent.SignOut) // Restart flow
                    }
                )
            }
        }
    }
}

/**
 * Ekran profilu dla niezalogowanego użytkownika
 */
@Composable
fun AnonymousProfileScreen(
    onLogin: (email: String, password: String) -> Unit,
    onSignUp: (email: String, password: String) -> Unit
) {
    var showLoginForm by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Ikona profilu",
            modifier = Modifier.height(72.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = if (showLoginForm) "Zaloguj się" else "Zarejestruj się",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Hasło") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                if (showLoginForm) {
                    onLogin(email, password)
                } else {
                    onSignUp(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (showLoginForm) "Zaloguj" else "Zarejestruj")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = { showLoginForm = !showLoginForm },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (showLoginForm) 
                    "Nie masz konta? Zarejestruj się" 
                else 
                    "Masz już konto? Zaloguj się"
            )
        }
    }
}

/**
 * Ekran profilu dla zalogowanego użytkownika
 */
@Composable
fun UserProfileScreen(
    user: User,
    settingsState: UserSettingsState,
    onSignOut: () -> Unit,
    onUpdateLastDrinkingDate: (String) -> Unit,
    onUpdateNotificationSettings: (Boolean) -> Unit,
    onUpdateSecuritySettings: (Boolean, String?) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Ikona użytkownika",
                        modifier = Modifier.height(72.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = user.email ?: "Użytkownik",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    if (user.lastDrinkingDate != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ostatnie picie: ${user.lastDrinkingDate}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
        }
        
        // Sekcja daty ostatniego picia
        item {
            Text(
                text = "Data ostatniego picia",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                LastDrinkingDateSection(
                    currentDate = user.lastDrinkingDate ?: LocalDate.now().toString(),
                    onDateSelected = { onUpdateLastDrinkingDate(it) }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Sekcja powiadomień
        item {
            Text(
                text = "Powiadomienia",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                ListItem(
                    headlineContent = { Text("Codzienne powiadomienia") },
                    supportingContent = { Text("Otrzymuj przypomnienia o odpowiedzi na pytanie \"Masz dziś kaca?\"") },
                    leadingContent = { 
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Ikona powiadomień"
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = settingsState.notificationsEnabled,
                            onCheckedChange = { onUpdateNotificationSettings(it) }
                        )
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Sekcja ustawień zabezpieczeń
        item {
            Text(
                text = "Ustawienia zabezpieczeń",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                ListItem(
                    headlineContent = { Text("Zabezpiecz aplikację kodem PIN") },
                    supportingContent = { Text("Wymagaj podania kodu PIN przy uruchomieniu aplikacji") },
                    leadingContent = { 
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ikona zabezpieczeń"
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = settingsState.securityPinEnabled,
                            onCheckedChange = { 
                                // W rzeczywistej implementacji tutaj byłby dialog do wpisania PIN-u
                                val pin = if (it) "1234" else null
                                onUpdateSecuritySettings(it, pin)
                            }
                        )
                    }
                )
                
                if (settingsState.securityPinEnabled) {
                    Divider()
                    
                    ListItem(
                        headlineContent = { Text("Aktualny kod PIN") },
                        supportingContent = { Text("${settingsState.securityPin ?: "Brak"} (uproszczone dla demo)") },
                        leadingContent = { 
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Ikona PIN"
                            )
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
        
        // Przycisk wylogowania
        item {
            Button(
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Wyloguj się")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Sekcja daty ostatniego picia
 */
@Composable
fun LastDrinkingDateSection(
    currentDate: String,
    onDateSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Ikona kalendarza",
                modifier = Modifier.padding(end = 8.dp)
            )
            
            Text(
                text = "Data ostatniego spożycia alkoholu",
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Aktualna data: $currentDate",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { 
                    // Ustawia dzisiejszą datę
                    onDateSelected(LocalDate.now().toString()) 
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                Text("Dzisiaj")
            }
            
            Button(
                onClick = { 
                    // Ustawia wczorajszą datę
                    onDateSelected(LocalDate.now().minusDays(1).toString()) 
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            ) {
                Text("Wczoraj")
            }
        }
    }
}

/**
 * Ekran wyświetlany w przypadku błędu
 */
@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Wystąpił błąd",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Button(onClick = onRetry) {
            Text("Spróbuj ponownie")
        }
    }
}
