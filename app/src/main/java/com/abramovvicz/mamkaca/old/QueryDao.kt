package com.abramovvicz.mamkaca.old

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QueryDao {
    @Insert
    suspend fun insert(query: QueryEntity)

    @Query("select * from QueryEntity")
    suspend fun getAll(): List<QueryEntity>
}