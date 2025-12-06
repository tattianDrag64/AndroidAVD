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
                repository.saveWeatherToDb(entity)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error loading weather", e)
                // Optionally, you can expose the error to the UI
            }
        }
    }

    fun deleteWeather(weather: WeatherEntity) {
        viewModelScope.launch {
            repository.deleteWeather(weather)
        }
    }
}
