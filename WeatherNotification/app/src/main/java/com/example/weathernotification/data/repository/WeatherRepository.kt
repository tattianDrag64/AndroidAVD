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
            apiKey = "e85d5ad8024cbbdbff457538e711f0cf"
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
