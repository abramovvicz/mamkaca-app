package com.abramovvicz.mamkaca

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext

// Retrofit Client
object RetrofitClient {
    private const val BASE_URL = "https://3.71.255.220:8080"
    // Interceptor do logowania adresów URL
    private val urlLoggingInterceptor = Interceptor { chain ->
        val request = chain.request()
        val url = request.url
        Log.d("RetrofitURL", "Request URL: $url")
        chain.proceed(request)
    }

    // Logging pełnego requesta i response (body, headers, itd.)
    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient z interceptorami
    private val client = OkHttpClient.Builder()
//        val certificateFactory = CertificateFactory.getInstance("X.509");
        .addInterceptor(urlLoggingInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .sslSocketFactory(
            SSLContext.getInstance("TLS").apply {
                init(null, arrayOf(createTrustManager(), null))
            }.socketFactory
        )
        .build()

    val api: MamKacaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MamKacaApi::class.java)
    }
}