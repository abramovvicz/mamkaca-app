package com.abramovvicz.mamkaca.presentation.screen.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abramovvicz.mamkaca.presentation.viewmodel.StatsIntent
import com.abramovvicz.mamkaca.presentation.viewmodel.StatsUIState
import com.abramovvicz.mamkaca.presentation.viewmodel.StatsViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

/**
 * Ekran statystyk trzeźwości użytkownika
 */
@Composable
fun StatsScreen() {
    // Pobieramy ViewModel za pomocą Koin
    val viewModel: StatsViewModel = koinViewModel()
    
    // Obserwujemy stan UI
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val state = uiState) {
            is StatsUIState.Loading -> {
                // Ekran ładowania
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is StatsUIState.StatsLoaded -> {
                // Ekran z załadowanymi statystykami
                StatsContent(
                    stats = state,
                    onPeriodSelected = { days ->
                        viewModel.handleIntent(StatsIntent.FilterByPeriod(days))
                    }
                )
            }
            
            is StatsUIState.Error -> {
                // Ekran z błędem
                ErrorScreen(
                    message = state.message,
                    onRetry = {
                        viewModel.handleIntent(StatsIntent.LoadStats)
                    }
                )
            }
        }
    }
}

/**
 * Zawartość ekranu ze statystykami
 */
@Composable
fun StatsContent(
    stats: StatsUIState.StatsLoaded,
    onPeriodSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Statystyki",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        // Wybór okresu
        PeriodSelector(
            currentPeriod = stats.periodDays,
            onPeriodSelected = onPeriodSelected
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (stats.totalAnswers == 0) {
            // Brak danych
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Brak danych w wybranym okresie",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Odpowiedz na pytanie \"Masz dziś kaca?\" aby zacząć zbierać statystyki.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Podstawowe statystyki
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Odpowiedzi w okresie ${stats.periodDays} dni",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    StatisticRow("Łącznie odpowiedzi", stats.totalAnswers.toString())
                    StatisticRow("Odpowiedzi TAK", stats.yesAnswers.toString())
                    StatisticRow("Odpowiedzi NIE", stats.noAnswers.toString())
                    StatisticRow("Procent TAK", "${stats.percentageYes.roundToInt()}%")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Statystyki ciągów
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Ciągi dni bez kaca",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    StatisticRow(
                        "Najdłuższy ciąg", 
                        "${stats.longestStreakDays} ${getDayText(stats.longestStreakDays)}"
                    )
                    
                    StatisticRow(
                        "Aktualny ciąg", 
                        "${stats.currentStreakDays} ${getDayText(stats.currentStreakDays)}"
                    )
                }
            }
        }
    }
}

/**
 * Wybór okresu dla statystyk
 */
@Composable
fun PeriodSelector(
    currentPeriod: Int,
    onPeriodSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Wybierz okres",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PeriodButton(text = "7 dni", days = 7, isSelected = currentPeriod == 7, onPeriodSelected = onPeriodSelected)
            PeriodButton(text = "30 dni", days = 30, isSelected = currentPeriod == 30, onPeriodSelected = onPeriodSelected)
            PeriodButton(text = "90 dni", days = 90, isSelected = currentPeriod == 90, onPeriodSelected = onPeriodSelected)
            PeriodButton(text = "365 dni", days = 365, isSelected = currentPeriod == 365, onPeriodSelected = onPeriodSelected)
        }
    }
}

/**
 * Przycisk wyboru okresu
 */
@Composable
fun PeriodButton(
    text: String, 
    days: Int, 
    isSelected: Boolean,
    onPeriodSelected: (Int) -> Unit
) {
    if (isSelected) {
        Button(
            onClick = { onPeriodSelected(days) },
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(text = text)
        }
    } else {
        OutlinedButton(
            onClick = { onPeriodSelected(days) },
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(text = text)
        }
    }
}

/**
 * Wiersz z pojedynczą statystyką
 */
@Composable
fun StatisticRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = value, fontWeight = FontWeight.Bold)
    }
    
    Divider()
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

/**
 * Pomocnicza funkcja do odmiany słowa "dzień"
 */
private fun getDayText(days: Int): String {
    return when {
        days == 1 -> "dzień"
        days in 2..4 || days in 22..24 -> "dni"
        else -> "dni"
    }
}
