package com.example.weathernotification.data

import android.content.Context
import com.example.weathernotification.data.repository.WeatherRepository

object DatabaseProvider {

    fun provideWeatherRepository(context: Context): WeatherRepository {
        val db = AppDatabase.getDatabase(context)
        return WeatherRepository(
            dao = db.weatherDao(),
            api = RetrofitClient.api
        )
    }
}
