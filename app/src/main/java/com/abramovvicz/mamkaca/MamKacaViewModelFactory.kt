package com.abramovvicz.mamkaca

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MamKacaViewModelFactory(
    private val deviceId: String,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MamKacaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MamKacaViewModel(deviceId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}