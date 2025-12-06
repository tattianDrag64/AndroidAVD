package com.example.weathernotification.data.dao

import androidx.room.*
import com.example.weathernotification.data.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather_table ORDER BY id DESC")
    fun getAllWeather(): Flow<List<WeatherEntity>>

    @Delete
    suspend fun deleteWeather(weather: WeatherEntity)

    @Update
    suspend fun updateWeather(weather: WeatherEntity)
}