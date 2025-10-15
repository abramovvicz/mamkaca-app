package com.abramovvicz.mamkaca.data.remote

import io.github.cdimascio.dotenv.dotenv

object Config {
    private val dotenv = dotenv {
        ignoreIfMissing = false // jeśli .env musi być obecny
    }

    val SUPABASE_URL: String = dotenv["SUPABASE_URL"] ?: error("SUPABASE_URL not found")
    val SUPABASE_KEY: String = dotenv["SUPABASE_KEY"] ?: error("SUPABASE_KEY not found")
}