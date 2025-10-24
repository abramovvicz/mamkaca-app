package com.abramovvicz.mamkaca.data.remote

import com.abramovvicz.mamkaca.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
//import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

object SupabaseClient {
    private val supabaseUrl get() = BuildConfig.SUPABASE_URL
    private val supabaseKey get() = BuildConfig.SUPABASE_KEY

    private lateinit var client: SupabaseClient

    fun initialize() {
        client = createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Postgrest)
//            install(GoTrue)
            install(Realtime)
        }
    }

    fun getClient(): SupabaseClient {
        check(::client.isInitialized) { "SupabaseClient nie został zainicjalizowany. Wywołaj najpierw initialize()." }
        return client
    }
}
