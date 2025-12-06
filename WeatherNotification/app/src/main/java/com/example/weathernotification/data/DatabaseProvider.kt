package com.example.weathernotification.data

import android.content.Context
import androidx.room.Room
import com.example.weathernotification.data.repository.CityRepository
import com.example.weathernotification.data.repository.WeatherRepository

object DatabaseProvider {

    private var database: AppDatabase? = null
    private var weatherRepository: WeatherRepository? = null
    private var cityRepository: CityRepository? = null

    private fun getDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            database ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "weather_db"
            ).fallbackToDestructiveMigration().build().also { database = it }
        }
    }

    fun provideWeatherRepository(context: Context): WeatherRepository {
        return weatherRepository ?: synchronized(this) {
            weatherRepository ?: WeatherRepository(
                dao = getDatabase(context).weatherDao(),
                api = RetrofitClient.api
            ).also { weatherRepository = it }
        }
    }

    fun provideCityRepository(context: Context): CityRepository {
        return cityRepository ?: synchronized(this) {
            cityRepository ?: CityRepository(
                cityDao = getDatabase(context).cityDao()
            ).also { cityRepository = it }
        }
    }
}
