package com.abramovvicz.mamkaca.data.mapper

import com.abramovvicz.mamkaca.domain.model.Answer
import com.abramovvicz.mamkaca.domain.model.AnswerSource
import com.abramovvicz.mamkaca.domain.model.AnswerType

/**
 * Klasa mapująca odpowiedzialna za konwersję między modelem domeny Answer a DTO.
 */
object AnswerMapper {
    
    /**
     * Konwertuje model domeny Answer na DTO.
     * W obecnej implementacji Answer jest już dostosowany do formatu API,
     * więc mapowanie jest tożsamościowe, ale struktura pozostaje dla spójności
     * i możliwości przyszłych modyfikacji.
     *
     * @param domainModel model domeny odpowiedzi
     * @return odpowiedź w formacie DTO (obecnie tożsamy z modelem domeny)
     */
    fun toEntity(domainModel: Answer): Answer {
        return domainModel
    }

    /**
     * Konwertuje DTO na model domeny Answer.
     * W obecnej implementacji Answer jest już dostosowany do formatu API,
     * więc mapowanie jest tożsamościowe, ale struktura pozostaje dla spójności
     * i możliwości przyszłych modyfikacji.
     *
     * @param entity DTO odpowiedzi
     * @return model domeny odpowiedzi
     */
    fun toDomain(entity: Answer): Answer {
        return entity
    }

    /**
     * Konwertuje listę DTO na listę modeli domeny Answer.
     *
     * @param entityList lista DTO odpowiedzi
     * @return lista modeli domeny odpowiedzi
     */
    fun toDomainList(entityList: List<Answer>): List<Answer> {
        return entityList.map { toDomain(it) }
    }
    
    /**
     * Sprawdza, czy dane modelu odpowiedzi są prawidłowe.
     * 
     * @param answer model odpowiedzi do walidacji
     * @return true, jeśli dane są prawidłowe
     * @throws IllegalArgumentException jeśli dane są nieprawidłowe
     */
    fun validate(answer: Answer) {
        if (answer.id.isBlank()) {
            throw IllegalArgumentException("ID odpowiedzi nie może być puste")
        }
        
        if (answer.answerDate.isBlank()) {
            throw IllegalArgumentException("Data odpowiedzi nie może być pusta")
        }
        
        if (answer.answerTime.isBlank()) {
            throw IllegalArgumentException("Czas odpowiedzi nie może być pusty")
        }
        
        // Sprawdź, czy przynajmniej jedno z userId lub deviceId jest ustawione
        if (answer.userId.isNullOrBlank() && answer.deviceId.isNullOrBlank()) {
            throw IllegalArgumentException("Przynajmniej jedno z ID użytkownika lub ID urządzenia musi być podane")
        }
        
        // Sprawdź, czy answerType jest prawidłowy
        validateAnswerType(answer.answerType)
        
        // Sprawdź, czy answerSource jest prawidłowy
        validateAnswerSource(answer.answerSource)
    }
    
    /**
     * Sprawdza, czy typ odpowiedzi jest prawidłowy.
     * 
     * @param answerType typ odpowiedzi do walidacji
     * @throws IllegalArgumentException jeśli typ jest nieprawidłowy
     */
    private fun validateAnswerType(answerType: AnswerType) {
        // Sprawdź, czy typ odpowiedzi jest jednym z dozwolonych typów
        when (answerType) {
            AnswerType.YES, AnswerType.NO, AnswerType.NONE -> {}
            else -> throw IllegalArgumentException("Nieprawidłowy typ odpowiedzi")
        }
    }
    
    /**
     * Sprawdza, czy źródło odpowiedzi jest prawidłowe.
     * 
     * @param answerSource źródło odpowiedzi do walidacji
     * @throws IllegalArgumentException jeśli źródło jest nieprawidłowe
     */
    private fun validateAnswerSource(answerSource: AnswerSource) {
        // Sprawdź, czy źródło odpowiedzi jest jednym z dozwolonych źródeł
        when (answerSource) {
            AnswerSource.MANUAL, AnswerSource.NOTIFICATION -> {}
            else -> throw IllegalArgumentException("Nieprawidłowe źródło odpowiedzi")
        }
    }
}
