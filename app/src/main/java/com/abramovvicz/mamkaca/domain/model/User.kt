package com.abramovvicz.mamkaca.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

/**
 * Model reprezentujący użytkownika aplikacji.
 */
@Serializable
data class User(
    val id: String,
    val email: String? = null,
    @SerialName("last_drinking_date")
    val lastDrinkingDate: String? = null,
    val timezone: String = "Europe/Warsaw",
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
) {
    companion object {
        /**
         * Tworzy nowy obiekt User z domyślnymi wartościami dla nowo utworzonego użytkownika.
         */
        fun createNew(id: String, email: String?): User {
            val now = ZonedDateTime.now().toString()
            return User(
                id = id,
                email = email,
                lastDrinkingDate = null,
                timezone = "Europe/Warsaw",
                createdAt = now,
                updatedAt = now
            )
        }
    }
}
