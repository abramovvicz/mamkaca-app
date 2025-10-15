package com.abramovvicz.mamkaca

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MamKacaViewModel(private val deviceId: String, application: Application) :
    AndroidViewModel(application) {
    private val api = RetrofitClient.api

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _todayStats = MutableStateFlow(Stats(0, 0, 0))
    val todayStats: StateFlow<Stats> = _todayStats.asStateFlow()

    private val _monthStats = MutableStateFlow(Stats(0, 0, 0))
    val monthStats: StateFlow<Stats> = _monthStats.asStateFlow()

    private val _yearStats = MutableStateFlow(Stats(0, 0, 0))
    val yearStats: StateFlow<Stats> = _yearStats.asStateFlow()

    private val _hasVotedToday = MutableStateFlow(false)
    val hasVotedToday: StateFlow<Boolean> = _hasVotedToday.asStateFlow()

    private val applicationContext: Context
        get() = getApplication<Application>().applicationContext

    @OptIn(DelicateCoroutinesApi::class)
    fun checkIfVotedToday() {
        _isLoading.value = true
        GlobalScope.launch {
            try {
                val response = api.checkVote(deviceId)
                _hasVotedToday.value = response.hasVoted
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun fetchTodayStats() {
        _isLoading.value = true
        GlobalScope.launch {
            try {
                val response = api.getTodayStats()
                _todayStats.value = response
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun fetchAllStats() {
        _isLoading.value = true
        GlobalScope.launch {
            try {
                val todayResponse = api.getTodayStats()
                val monthResponse = api.getMonthStats()
                val yearResponse = api.getYearStats()

                _todayStats.value = todayResponse
                _monthStats.value = monthResponse
                _yearStats.value = yearResponse
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun vote(hasHangover: Boolean) {
        _isLoading.value = true
        GlobalScope.launch {
            try {
                api.healthCheck();
                val vote = Vote(deviceId, hasHangover)
                api.sendVote(vote)
                _hasVotedToday.value = true
                fetchTodayStats()

                // Show toast
                val message = if (hasHangover) {
                    "Głos oddany. Trzymaj się!"
                } else {
                    "Głos oddany. Tak trzymaj!"
                }
                showToast(message)
            } catch (e: Exception) {
                showToast("Błąd podczas głosowania. Spróbuj ponownie.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun showToast(message: String) {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}