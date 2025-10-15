package com.abramovvicz.mamkaca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(viewModel: MamKacaViewModel, navController: androidx.navigation.NavController) {
    val isLoading by viewModel.isLoading.collectAsState()
    val todayStats by viewModel.todayStats.collectAsState()
    val monthStats by viewModel.monthStats.collectAsState()
    val yearStats by viewModel.yearStats.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAllStats()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statystyki") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Warning, contentDescription = "Główna") },
                    label = { Text("Główna") },
                    selected = false,
                    onClick = { navController.navigate("main") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Statystyki") },
                    label = { Text("Statystyki") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = "O aplikacji") },
                    label = { Text("O aplikacji") },
                    selected = false,
                    onClick = { navController.navigate("about") }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Statystyki kaca",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    StatCard(
                        title = "Dzisiaj",
                        totalVotes = todayStats.totalVotes,
                        positiveVotes = todayStats.positiveVotes,
                        negativeVotes = todayStats.negativeVotes
                    )

                    StatCard(
                        title = "Ten miesiąc",
                        totalVotes = monthStats.totalVotes,
                        positiveVotes = monthStats.positiveVotes,
                        negativeVotes = monthStats.negativeVotes
                    )

                    StatCard(
                        title = "Ten rok",
                        totalVotes = yearStats.totalVotes,
                        positiveVotes = yearStats.positiveVotes,
                        negativeVotes = yearStats.negativeVotes
                    )
                }
            }
        }
    }
}