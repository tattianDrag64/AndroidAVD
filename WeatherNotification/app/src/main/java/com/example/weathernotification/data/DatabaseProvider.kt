package com.example.weathernotification.data

import android.content.Context
import androidx.room.Room
import com.example.weathernotification.data.repository.WeatherRepository

object DatabaseProvider {

    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "weather_db"
            ).build()
            INSTANCE = instance
            instance
        }
    }

    fun provideWeatherRepository(context: Context): WeatherRepository {
        val db = AppDatabase.getDatabase(context)
        return WeatherRepository(
            dao = db.weatherDao(),
            api = RetrofitClient.api
        )
    }
}