// MainActivity.kt
package com.abramovvicz.mamkaca

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deviceId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )

        setContent {
            MamKacaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(deviceId, application)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(deviceId: String, application: Application) {
    val navController = rememberNavController()
    val viewModel: MamKacaViewModel = viewModel(
        factory = MamKacaViewModelFactory(deviceId, application)
    )

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(viewModel, navController)
        }
        composable("statistics") {
            StatisticsScreen(viewModel, navController)
        }
        composable("about") {
            AboutScreen(navController)
        }
    }
}

// Data Classes
data class Stats(
    val totalVotes: Int,
    val positiveVotes: Int,
    val negativeVotes: Int
)

data class Vote(
    val deviceId: String,
    val hasHangover: Boolean
)

data class VoteCheck(
    val hasVoted: Boolean
)