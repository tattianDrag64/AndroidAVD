package com.example.weathernotification.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val city: String,
    val temp: Double,
    val wind: Double,
    val time: String
)
