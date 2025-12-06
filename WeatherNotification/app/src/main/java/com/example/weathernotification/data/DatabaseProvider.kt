package com.example.weathernotification.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    private var database: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return database ?: Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "weather_db"
        ).build().also { database = it }
    }
}
