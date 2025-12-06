package com.example.weathernotification.data.dao

import androidx.room.*
import com.example.weathernotification.data.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)

    @Delete
    suspend fun deleteCity(city: CityEntity)

    @Query("SELECT * FROM cities")
    fun getAllCities(): Flow<List<CityEntity>>
}