package com.abramovvicz.mamkaca.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abramovvicz.mamkaca.domain.model.AnswerType
import com.abramovvicz.mamkaca.presentation.viewmodel.HomeIntent
import com.abramovvicz.mamkaca.presentation.viewmodel.HomeUIState
import com.abramovvicz.mamkaca.presentation.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Główny ekran aplikacji z pytaniem "Masz dziś kaca?"
 */
@Composable
fun HomeScreen() {
    // Pobieramy ViewModel za pomocą Koin
    val viewModel: HomeViewModel = koinViewModel()
    
    // Obserwujemy stan UI
    val uiState by viewModel.uiState.collectAsState()
    
    // Obserwujemy stan formularza
    val formState by viewModel.formState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val state = uiState) {
            is HomeUIState.Loading -> {
                // Ekran ładowania
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is HomeUIState.ReadyToAnswer -> {
                // Ekran z pytaniem
                QuestionScreen(
                    onAnswerYes = {
                        // Odpowiedź "Tak" - przejdź do ekranu z notatką
                        viewModel.handleIntent(HomeIntent.UpdateNote(""))
                        viewModel.handleIntent(HomeIntent.SubmitAnswer(AnswerType.YES))
                    },
                    onAnswerNo = {
                        // Odpowiedź "Nie" - zapisz od razu
                        viewModel.handleIntent(HomeIntent.SubmitAnswer(AnswerType.NO))
                    }
                )
            }
            
            is HomeUIState.AnswerSaved -> {
                // Ekran po zapisaniu odpowiedzi
                AnsweredScreen(
                    answerType = state.answerType,
                    onReset = {
                        // Tylko do celów demonstracyjnych - resetowanie odpowiedzi
                        viewModel.handleIntent(HomeIntent.ResetAnswer)
                    }
                )
            }
            
            is HomeUIState.Error -> {
                // Ekran z błędem
                ErrorScreen(
                    message = state.message,
                    onRetry = {
                        // Ponowne załadowanie
                        viewModel.loadTodayAnswer()
                    }
                )
            }
        }
    }
}

/**
 * Ekran z pytaniem "Masz dziś kaca?"
 */
@Composable
fun QuestionScreen(
    onAnswerYes: () -> Unit,
    onAnswerNo: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Masz dziś kaca?",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onAnswerYes,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("TAK")
            }
            
            Button(
                onClick = onAnswerNo,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text("NIE")
            }
        }
    }
}

/**
 * Ekran wyświetlany po odpowiedzi "Tak", gdzie użytkownik może dodać notatkę
 */
@Composable
fun YesAnswerDetailsScreen(
    note: String,
    onNoteChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Możesz dodać krótką notatkę",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        OutlinedTextField(
            value = note,
            onValueChange = onNoteChange,
            label = { Text("Opcjonalna notatka") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("Anuluj")
            }
            
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text("Zapisz")
            }
        }
    }
}

/**
 * Ekran wyświetlany po udzieleniu odpowiedzi
 */
@Composable
fun AnsweredScreen(
    answerType: AnswerType,
    onReset: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val text = when (answerType) {
                AnswerType.YES -> "Dziękujemy za odpowiedź. Pamiętaj, że zawsze możesz skorzystać z dostępnych zasobów pomocy."
                AnswerType.NO -> "Świetnie! Twój okres trzeźwości trwa. Tak trzymaj!"
                else -> "Dziękujemy za odpowiedź."
            }
            
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // To jest tylko do celów demo, normalnie użytkownik nie powinien móc zmieniać odpowiedzi
            Button(onClick = onReset) {
                Text("Zresetuj odpowiedź (tylko demo)")
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
