package com.abramovvicz.mamkaca.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.abramovvicz.mamkaca.presentation.screen.MainScreen
import com.abramovvicz.mamkaca.presentation.theme.MamKacaTheme

/**
 * Główna aktywność aplikacji MamKaca.
 * Zawiera główny graf nawigacyjny i ustawia temat aplikacji.
 */
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MamKacaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}
