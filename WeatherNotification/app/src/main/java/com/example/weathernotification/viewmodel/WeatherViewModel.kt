package com.example.weathernotification.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathernotification.data.entity.WeatherEntity
import com.example.weathernotification.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    //private mutable state flow for the current weather
    private val _weather = MutableStateFlow<WeatherEntity?>(null)
    //public immutable state flow for the current weather
    val weather = _weather.asStateFlow()

    //private mutable state flow for error messages
    private val _error = MutableStateFlow<String?>(null)
    //public immutable state flow for error messages
    val error = _error.asStateFlow()

    //flow of saved weather from the repository, converted to a stateIn for sharing
    val savedWeather = repository
        .getSavedWeather()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    //manually sets the current weather, used when clicking an item in the saved list
    fun setWeather(weather: WeatherEntity) {
        _weather.value = weather
    }

    //clears the current weather state
    fun clearWeather() {
        _weather.value = null
    }

    //clears the error state
    fun clearError() {
        _error.value = null
    }

    //loads weather data from the api for a given city
    fun loadWeather(city: String) {
        viewModelScope.launch {
            try {
                val response = repository.loadWeatherFromApi(city)

                //map the api response to our local WeatherEntity
                val entity = WeatherEntity(
                    city = response.name,
                    temp = response.main.temp,
                    wind = response.wind.speed,
                    time = System.currentTimeMillis().toString(),
                    description = response.weather.firstOrNull()?.description ?: ""
                )
                _weather.value = entity
            } catch (e: Exception) {
                //update the error state if loading fails
                _error.value = "City not found or invalid name"
                Log.e("WeatherViewModel", "Error loading weather", e)
            }
        }
    }

    //creates a WeatherEntity and saves it to the database
    fun saveWeather(city: String, temp: Double, wind: Double, description: String) {
        viewModelScope.launch {
            val entity = WeatherEntity(
                city = city,
                temp = temp,
                wind = wind,
                time = System.currentTimeMillis().toString(),
                description = description
            )
            repository.saveWeatherToDb(entity)
        }
    }

    //deletes a weather entity from the database
    fun deleteWeather(weather: WeatherEntity) {
        viewModelScope.launch {
            repository.deleteWeather(weather)
            //if the deleted weather was the one being displayed, clear the display
            if (_weather.value?.id == weather.id) {
                _weather.value = null
            }
        }
    }

    //updates an existing weather entity by fetching fresh data from the api
    fun updateWeather(weather: WeatherEntity) {
        viewModelScope.launch {
            try {
                val response = repository.loadWeatherFromApi(weather.city)
                //create an updated entity and pass it to the repository
                val updatedEntity = weather.copy(
                    temp = response.main.temp,
                    wind = response.wind.speed,
                    time = System.currentTimeMillis().toString(),
                    description = response.weather.firstOrNull()?.description ?: ""
                )
                repository.updateWeather(updatedEntity)
            } catch (e: Exception) {
                //handle update errors
                _error.value = "Could not update weather"
                Log.e("WeatherViewModel", "Error updating weather", e)
            }
        }
    }
}