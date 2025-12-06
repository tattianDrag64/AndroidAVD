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

    private val _weather = MutableStateFlow<WeatherEntity?>(null)
    val weather = _weather.asStateFlow()

    val savedWeather = repository
        .getSavedWeather()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setWeather(weather: WeatherEntity) {
        _weather.value = weather
    }

    fun clearWeather() {
        _weather.value = null
    }

    fun loadWeather(city: String) {
        viewModelScope.launch {
            try {
                val response = repository.loadWeatherFromApi(city)

                val entity = WeatherEntity(
                    city = response.name,
                    temp = response.main.temp,
                    wind = response.wind.speed,
                    time = System.currentTimeMillis().toString()
                )
                _weather.value = entity
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error loading weather", e)
            }
        }
    }

    fun saveWeather(city: String, temp: Double, wind: Double) {
        viewModelScope.launch {
            val entity = WeatherEntity(
                city = city,
                temp = temp,
                wind = wind,
                time = System.currentTimeMillis().toString()
            )
            repository.saveWeatherToDb(entity)
        }
    }

    fun deleteWeather(weather: WeatherEntity) {
        viewModelScope.launch {
            repository.deleteWeather(weather)
            if (_weather.value?.id == weather.id) {
                _weather.value = null
            }
        }
    }

    fun updateWeather(weather: WeatherEntity) {
        viewModelScope.launch {
            try {
                val response = repository.loadWeatherFromApi(weather.city)
                val updatedEntity = weather.copy(
                    temp = response.main.temp,
                    wind = response.wind.speed,
                    time = System.currentTimeMillis().toString()
                )
                repository.updateWeather(updatedEntity)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error updating weather", e)
            }
        }
    }
}