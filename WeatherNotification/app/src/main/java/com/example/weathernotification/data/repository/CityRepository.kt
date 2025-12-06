package com.example.weathernotification.data.repository

import com.example.weathernotification.data.dao.CityDao
import com.example.weathernotification.data.entity.CityEntity

class CityRepository(
    private val cityDao: CityDao
) {

    fun getAllCities() = cityDao.getAllCities()

    suspend fun addCity(city: CityEntity) {
        cityDao.insertCity(city)
    }

    suspend fun deleteCity(city: CityEntity) {
        cityDao.deleteCity(city)
    }
}

