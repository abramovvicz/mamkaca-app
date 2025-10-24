package com.abramovvicz.mamkaca.di

import com.abramovvicz.mamkaca.data.repository.AnswerRepositoryImpl
import com.abramovvicz.mamkaca.data.repository.StatisticsRepositoryImpl
import com.abramovvicz.mamkaca.data.repository.UserRepositoryImpl
import com.abramovvicz.mamkaca.domain.repository.AnswerRepository
import com.abramovvicz.mamkaca.domain.repository.StatisticsRepository
import com.abramovvicz.mamkaca.domain.repository.UserRepository
import org.koin.dsl.module

/**
 * Moduł Koin dostarczający instancje repozytoriów.
 */
val repositoryModule = module {
    // Pojedyncza instancja repozytorium użytkownika
    single<UserRepository> { 
        UserRepositoryImpl() 
    }
    
    // Pojedyncza instancja repozytorium odpowiedzi
    single<AnswerRepository> { 
        AnswerRepositoryImpl() 
    }
    
    // Pojedyncza instancja repozytorium statystyk
    single<StatisticsRepository> { 
        StatisticsRepositoryImpl() 
    }
}
