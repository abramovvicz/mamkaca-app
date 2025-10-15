package com.abramovvicz.mamkaca.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

/**
 * Singleton odpowiedzialny za inicjalizację i dostarczenie klienta Supabase.
 */
object SupabaseClient {


    private lateinit var client: SupabaseClient

    /**
     * Inicjalizuje klienta Supabase z niezbędnymi modułami.
     */
    fun initialize() {
        client = createSupabaseClient(
            supabaseUrl = Config.SUPABASE_URL,
            supabaseKey = Config.SUPABASE_KEY
        ) {
            install(Postgrest)
            install(GoTrue)
            install(Realtime)
        }
    }

    /**
     * Zwraca instancję klienta Supabase.
     * @throws IllegalStateException jeśli klient nie został zainicjalizowany
     */
    fun getClient(): SupabaseClient {
        if (!::client.isInitialized) {
            throw IllegalStateException("SupabaseClient nie został zainicjalizowany. Wywołaj najpierw initialize().")
        }
        return client
    }
}
