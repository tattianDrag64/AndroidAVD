package com.example.weathernotification.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WeatherScreen(
    lat: Double,
    lon: Double,
    cityName: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(text = cityName, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Latitude: $lat")
        Text(text = "Longitude: $lon")

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Weather will be here soon 🌤")
    }
}
