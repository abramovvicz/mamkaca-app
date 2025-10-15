package com.abramovvicz.mamkaca

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// API Service
interface MamKacaApi {
    @GET("/api/stats/today")
    suspend fun getTodayStats(): Stats

    @GET("/api/stats/month")
    suspend fun getMonthStats(): Stats

    @GET("/api/stats/year")
    suspend fun getYearStats(): Stats

    @GET("/api/stats/range")
    suspend fun getRangeStats(@Query("from") from: String, @Query("to") to: String): Stats

    @GET("/api/stats/range/daily")
    suspend fun getDailyRangeStats(@Query("from") from: String, @Query("to") to: String): Stats

    @POST("/api/votes")
    suspend fun sendVote(@Body vote: Vote)

    @GET("/actuator/health")
    suspend fun healthCheck();

    @GET("/api/votes/check")
    suspend fun checkVote(@Query("deviceId") deviceId: String): VoteCheck
}