package com.abramovvicz.mamkaca

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Theme
@Composable
fun MamKacaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF81C784),
            primaryContainer = Color(0xFF1B5E20),
            onPrimaryContainer = Color.White,
            secondary = Color(0xFFFF5722),
            secondaryContainer = Color(0xFF5D4037),
            onSecondaryContainer = Color.White,
            tertiary = Color(0xFF2196F3),
            tertiaryContainer = Color(0xFF0D47A1),
            onTertiaryContainer = Color.White,
            background = Color(0xFF121212),
            surface = Color(0xFF212121),
            surfaceVariant = Color(0xFF323232)
        ),
        content = content
    )
}