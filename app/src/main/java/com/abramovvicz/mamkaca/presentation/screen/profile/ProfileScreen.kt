package com.abramovvicz.mamkaca.presentation.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Ekran profilu użytkownika
 */
@Composable
fun ProfileScreen() {
    // W przyszłości: stan zalogowania będzie pobierany z ViewModel
    var isLoggedIn by remember { mutableStateOf(false) }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isLoggedIn) {
            UserProfileScreen(
                onLogout = { isLoggedIn = false }
            )
        } else {
            AnonymousProfileScreen(
                onLogin = { isLoggedIn = true }
            )
        }
    }
}

/**
 * Ekran profilu dla zalogowanego użytkownika
 */
@Composable
fun UserProfileScreen(onLogout: () -> Unit) {
    // W przyszłości: dane użytkownika będą pobierane z ViewModel
    val email = "przyklad@email.pl"
    
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
                        text = email,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
        }
        
        // Sekcja powiadomień
        item {
            var notificationsEnabled by remember { mutableStateOf(true) }
            
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
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it }
                        )
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Sekcja ustawień
        item {
            Text(
                text = "Ustawienia",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                ListItem(
                    headlineContent = { Text("Zabezpiecz aplikację") },
                    supportingContent = { Text("Ustaw kod PIN do zabezpieczenia aplikacji") },
                    leadingContent = { 
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ikona ustawień"
                        )
                    }
                )
                
                Divider()
                
                ListItem(
                    headlineContent = { Text("Wydatki na alkohol") },
                    supportingContent = { Text("Ustaw typowe wydatki, aby zobaczyć oszczędności") },
                    leadingContent = { 
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ikona ustawień"
                        )
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
        
        // Przycisk wylogowania
        item {
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Wyloguj się")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Ekran profilu dla niezalogowanego użytkownika
 */
@Composable
fun AnonymousProfileScreen(onLogin: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = "Ikona email",
            modifier = Modifier.height(72.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Zaloguj się, aby korzystać z dodatkowych funkcji",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Synchronizacja między urządzeniami, dodatkowe statystyki i więcej!",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Zaloguj się / Zarejestruj")
        }
    }
}
