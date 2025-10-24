package com.abramovvicz.mamkaca.data.mapper

import com.abramovvicz.mamkaca.domain.model.User

/**
 * Klasa mapująca odpowiedzialna za konwersję między modelem domeny User a DTO.
 */
object UserMapper {
    
    /**
     * Konwertuje model domeny User na DTO.
     * W obecnej implementacji User jest już dostosowany do formatu API,
     * więc mapowanie jest tożsamościowe, ale struktura pozostaje dla spójności
     * i możliwości przyszłych modyfikacji.
     *
     * @param domainModel model domeny użytkownika
     * @return użytkownik w formacie DTO (obecnie tożsamy z modelem domeny)
     */
    fun toEntity(domainModel: User): User {
        return domainModel
    }

    /**
     * Konwertuje DTO na model domeny User.
     * W obecnej implementacji User jest już dostosowany do formatu API,
     * więc mapowanie jest tożsamościowe, ale struktura pozostaje dla spójności
     * i możliwości przyszłych modyfikacji.
     *
     * @param entity DTO użytkownika
     * @return model domeny użytkownika
     */
    fun toDomain(entity: User): User {
        return entity
    }

    /**
     * Konwertuje listę DTO na listę modeli domeny User.
     *
     * @param entityList lista DTO użytkowników
     * @return lista modeli domeny użytkowników
     */
    fun toDomainList(entityList: List<User>): List<User> {
        return entityList.map { toDomain(it) }
    }
    
    /**
     * Sprawdza, czy dane modelu użytkownika są prawidłowe.
     * 
     * @param user model użytkownika do walidacji
     * @return true, jeśli dane są prawidłowe
     * @throws IllegalArgumentException jeśli dane są nieprawidłowe
     */
    fun validate(user: User) {
        if (user.id.isBlank()) {
            throw IllegalArgumentException("ID użytkownika nie może być puste")
        }
    }
}
