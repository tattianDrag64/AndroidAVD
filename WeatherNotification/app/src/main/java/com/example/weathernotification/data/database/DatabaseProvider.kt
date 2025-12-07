package com.example.weathernotification.data.database

import android.content.Context
import androidx.room.Room
import com.example.weathernotification.data.repository.CityRepository
import com.example.weathernotification.data.repository.WeatherRepository

//singleton object to provide database and repository instances
object DatabaseProvider {

    //singleton instances of the database and repositories
    private var database: AppDatabase? = null
    private var weatherRepository: WeatherRepository? = null
    private var cityRepository: CityRepository? = null

    //private function to get a singleton instance of the database
    private fun getDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            database ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "weather_db"
            ).fallbackToDestructiveMigration().build().also { database = it }
        }
    }

    //provides a singleton instance of WeatherRepository
    fun provideWeatherRepository(context: Context): WeatherRepository {
        return weatherRepository ?: synchronized(this) {
            weatherRepository ?: WeatherRepository(
                dao = getDatabase(context).weatherDao(),
                api = RetrofitClient.api
            ).also { weatherRepository = it }
        }
    }

    //provides a singleton instance of CityRepository
    fun provideCityRepository(context: Context): CityRepository {
        return cityRepository ?: synchronized(this) {
            cityRepository ?: CityRepository(
                cityDao = getDatabase(context).cityDao()
            ).also { cityRepository = it }
        }
    }
}
