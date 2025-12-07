package com.example.weathernotification.data.repository

import com.example.weathernotification.data.dao.WeatherDao
import com.example.weathernotification.data.entity.WeatherEntity
import com.example.weathernotification.data.remote.api.WeatherApi
import com.example.weathernotification.data.remote.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

//repository to manage weather data from remote and local sources
class WeatherRepository(
    private val dao: WeatherDao,
    private val api: WeatherApi
) {

    //loads weather data from the remote api
    suspend fun loadWeatherFromApi(city: String): WeatherResponse {
        return api.getWeather(
            city = city,
            //todo: it's better to move the api key to a more secure place like build.gradle
            apiKey = "e85d5ad8024cbbdbff457538e711f0cf"
        )
    }

    //saves weather data to the local room database
    suspend fun saveWeatherToDb(weather: WeatherEntity) {
        dao.insertWeather(weather)
    }

    //retrieves all saved weather from the room database as a flow
    fun getSavedWeather(): Flow<List<WeatherEntity>> {
        return dao.getAllWeather()
    }

    //deletes a weather entry from the room database
    suspend fun deleteWeather(weather: WeatherEntity) {
        dao.deleteWeather(weather)
    }

    //updates a weather entry in the room database
    suspend fun updateWeather(weather: WeatherEntity) {
        dao.updateWeather(weather)
    }
}
