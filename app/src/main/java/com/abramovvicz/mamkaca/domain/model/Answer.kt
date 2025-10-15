package com.abramovvicz.mamkaca.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.UUID

/**
 * Model reprezentujący odpowiedź użytkownika na pytanie "Masz dziś kaca?".
 */
@Serializable
data class Answer(
    val id: String = UUID.randomUUID().toString(),
    
    @SerialName("user_id")
    val userId: String? = null,
    
    @SerialName("device_id") 
    val deviceId: String? = null,
    
    @SerialName("answer_date")
    val answerDate: String,
    
    @SerialName("answer_time")
    val answerTime: String,
    
    @SerialName("answer_type")
    val answerType: AnswerType,
    
    @SerialName("answer_source")
    val answerSource: AnswerSource = AnswerSource.MANUAL,
    
    val note: String? = null,
    
    @SerialName("created_at")
    val createdAt: String,
    
    @SerialName("updated_at")
    val updatedAt: String
) {
    companion object {
        /**
         * Tworzy nową odpowiedź na podstawie podanych parametrów.
         */
        fun create(
            userId: String? = null,
            deviceId: String? = null,
            answerType: AnswerType,
            note: String? = null,
            answerSource: AnswerSource = AnswerSource.MANUAL
        ): Answer {
            val now = ZonedDateTime.now()
            val date = LocalDate.now().toString()
            val time = LocalTime.now().toString()
            
            return Answer(
                id = UUID.randomUUID().toString(),
                userId = userId,
                deviceId = deviceId,
                answerDate = date,
                answerTime = time,
                answerType = answerType,
                answerSource = answerSource,
                note = note,
                createdAt = now.toString(),
                updatedAt = now.toString()
            )
        }
    }
}

/**
 * Typy odpowiedzi na pytanie "Masz dziś kaca?".
 */
@Serializable
enum class AnswerType(val value: String) {
    @SerialName("yes") 
    YES("yes"),
    
    @SerialName("no") 
    NO("no"),
    
    @SerialName("none") 
    NONE("none");
}

/**
 * Źródła odpowiedzi.
 */
@Serializable
enum class AnswerSource(val value: String) {
    @SerialName("manual") 
    MANUAL("manual"),
    
    @SerialName("notification") 
    NOTIFICATION("notification");
}
