package com.abramovvicz.mamkaca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private const val glownaText = "Główna"
private const val statystykiText = "Statystyki"
private const val oAplikacjiText = "O aplikacji"

private const val oAplikacjiOpis =
    "MamKaca.pl to aplikacja, która ma na celu walkę z alkoholizmem poprzez uświadamianie skali problemu i zachęcanie do trzeźwości."

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: androidx.navigation.NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(oAplikacjiText) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Warning, contentDescription = glownaText) },
                    label = { Text(glownaText) },
                    selected = false,
                    onClick = { navController.navigate("main") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = statystykiText) },
                    label = { Text(statystykiText) },
                    selected = false,
                    onClick = { navController.navigate("statistics") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = oAplikacjiText) },
                    label = { Text(oAplikacjiText) },
                    selected = true,
                    onClick = { }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "O aplikacji MamKaca.pl",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = oAplikacjiOpis,
                fontSize = 16.sp
            )

            Text(
                text = "Zasady korzystania:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "1. Każdy użytkownik może głosować tylko raz dziennie\n" +
                        "2. Głosowanie jest anonimowe, nie zbieramy danych osobowych\n" +
                        "3. Aplikacja służy jedynie do celów statystycznych i edukacyjnych",
                fontSize = 16.sp
            )

            Text(
                text = "Gdzie szukać pomocy?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Ogólnopolski Telefon dla Osób z Problemem Alkoholowym:\n800 120 289\n\n" +
                        "Anonimowi Alkoholicy:\nwww.aa.org.pl\n\n" +
                        "Państwowa Agencja Rozwiązywania Problemów Alkoholowych:\nwww.parpa.pl",
                fontSize = 16.sp
            )

            Text(
                text = "Wersja aplikacji: 1.0.0",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            )
        }
    }
}