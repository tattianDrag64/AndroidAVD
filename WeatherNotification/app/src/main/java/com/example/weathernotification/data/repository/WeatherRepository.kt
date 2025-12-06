package com.example.weathernotification.data.repository

import com.example.weathernotification.data.dao.WeatherDao
import com.example.weathernotification.data.entity.WeatherEntity
import com.example.weathernotification.data.remote.api.WeatherApi
import com.example.weathernotification.data.remote.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private val dao: WeatherDao,
    private val api: WeatherApi
) {
    suspend fun loadWeatherFromApi(city: String): WeatherResponse {
        return api.getWeather(
            city = city,
            apiKey = "b6907d289e10d714a6e88b30761fae22"
        )
    }

    // saving in room
    suspend fun saveWeatherToDb(weather: WeatherEntity) {
        dao.insertWeather(weather)
    }

    // accepting in room
    fun getSavedWeather(): Flow<List<WeatherEntity>> {
        return dao.getAllWeather()
    }

    // deleting from room
    suspend fun deleteWeather(weather: WeatherEntity) {
        dao.deleteWeather(weather)
    }
}
