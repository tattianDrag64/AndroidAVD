package com.example.weathernotification.data.remote.model

data class WeatherResponse(
    val name: String,
    val main: Main,
    val wind: Wind,
    val weather: List<WeatherDescription>
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class Wind(
    val speed: Double
)

data class WeatherDescription(
    val description: String
)
