package com.abramovvicz.mamkaca.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abramovvicz.mamkaca.presentation.screen.home.HomeScreen
import com.abramovvicz.mamkaca.presentation.screen.profile.ProfileScreen
import com.abramovvicz.mamkaca.presentation.screen.stats.StatsScreen

/**
 * Główny ekran aplikacji zawierający nawigację dolną i hoszczący NavHost.
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                NavigationItem.values().forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                // Unikaj tworzenia wielu kopii tej samej destynacji
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Unikaj duplikowania tej samej destynacji gdy klikamy ponownie ten sam element
                                launchSingleTop = true
                                // Przywróć stan, jeśli został zapisany
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = NavigationItem.Home.route
            ) {
                composable(NavigationItem.Home.route) {
                    HomeScreen()
                }
                composable(NavigationItem.Stats.route) {
                    StatsScreen()
                }
                composable(NavigationItem.Profile.route) {
                    ProfileScreen()
                }
            }
        }
    }
}

/**
 * Elementy nawigacji dolnej.
 */
enum class NavigationItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Home("home", "Dzisiaj", Icons.Filled.Home),
    Stats("stats", "Statystyki", Icons.Filled.Info),
    Profile("profile", "Profil", Icons.Filled.Person)
}
