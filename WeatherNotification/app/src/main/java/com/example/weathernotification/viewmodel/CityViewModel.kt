package com.example.weathernotification.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathernotification.data.DatabaseProvider
import com.example.weathernotification.data.entity.CityEntity
import kotlinx.coroutines.launch

class CityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DatabaseProvider.provideCityRepository(application)

    val cities = repository.getAllCities()

    fun addCity(name: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.addCity(
                CityEntity(name = name, latitude = lat, longitude = lon)
            )
        }
    }

    fun deleteCity(city: CityEntity) {
        viewModelScope.launch {
            repository.deleteCity(city)
        }
    }
}
