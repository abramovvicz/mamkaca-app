package com.abramovvicz.mamkaca

import android.app.Application
import com.abramovvicz.mamkaca.data.remote.SupabaseClient
import com.abramovvicz.mamkaca.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Główna klasa aplikacji MamKaca.
 * Inicjalizuje komponenty na poziomie aplikacji, takie jak klient Supabase.
 */
class MamKacaApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Inicjalizacja komponentów aplikacji
        SupabaseClient.initialize()
        
        // Inicjalizacja Koin dla wstrzykiwania zależności
        startKoin {
            androidLogger(Level.ERROR) // Poziom logowania
            androidContext(this@MamKacaApplication)
            modules(listOf(
                repositoryModule
                // Tutaj można dodać kolejne moduły w przyszłości
            ))
        }
    }
}
