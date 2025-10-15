package com.abramovvicz.mamkaca.old

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [QueryEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun queryDao(): QueryDao
}