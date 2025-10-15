package com.abramovvicz.mamkaca

import android.app.Application
import com.abramovvicz.mamkaca.data.remote.SupabaseClient

/**
 * Główna klasa aplikacji MamKaca.
 * Inicjalizuje komponenty na poziomie aplikacji, takie jak klient Supabase.
 */
class MamKacaApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Inicjalizacja komponentów aplikacji
        SupabaseClient.initialize()
    }
}
