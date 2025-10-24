package com.abramovvicz.mamkaca.di

import com.abramovvicz.mamkaca.presentation.viewmodel.HomeViewModel
import com.abramovvicz.mamkaca.presentation.viewmodel.ProfileViewModel
import com.abramovvicz.mamkaca.presentation.viewmodel.StatsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Moduł Koin dostarczający instancje ViewModeli.
 */
val viewModelModule = module {
    // HomeViewModel z wstrzykniętym AnswerRepository
    viewModel { 
        HomeViewModel(answerRepository = get()) 
    }
    
    // StatsViewModel z wstrzykniętym AnswerRepository
    viewModel { 
        StatsViewModel(answerRepository = get()) 
    }
    
    // ProfileViewModel z wstrzykniętym UserRepository
    viewModel { 
        ProfileViewModel(userRepository = get()) 
    }
}
