package com.abramovvicz.mamkaca.old

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QueryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val query:String
)