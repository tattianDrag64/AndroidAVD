package com.example.weathernotification.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weathernotification.data.entity.WeatherEntity
import com.example.weathernotification.viewmodel.Theme
import com.example.weathernotification.viewmodel.ThemeViewModel
import com.example.weathernotification.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel,
    themeViewModel: ThemeViewModel,
    navController: NavController,
    defaultCity: String = "",
    weatherItem: WeatherEntity? = null,
    onBack: (() -> Unit)? = null
) {

    val weather by weatherViewModel.weather.collectAsState()
    val savedWeather by weatherViewModel.savedWeather.collectAsState()
    var city by remember { mutableStateOf(defaultCity) }

    val currentTheme by themeViewModel.theme.collectAsState()

    LaunchedEffect(defaultCity, weatherItem) {
        if (weatherItem != null) {
            weatherViewModel.setWeather(weatherItem)
        } else if (defaultCity.isNotBlank()) {
            weatherViewModel.loadWeather(defaultCity)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather") },
                actions = {
                    IconButton(onClick = { themeViewModel.toggleTheme() }) {
                        Icon(
                            imageVector = if (currentTheme == Theme.DARK) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Insert city") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (city.isNotBlank()) {
                        weatherViewModel.loadWeather(city)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Show weather")
            }

            Button(
                onClick = { navController.navigate("saved") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Saved Locations")
            }

            Spacer(modifier = Modifier.height(16.dp))

            weather?.let { data ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            text = "City: ${data.city}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Temperature: ${data.temp} °C"
                        )

                        Text(
                            text = "Wind: ${data.wind} m/s"
                        )
                    }
                }

                val isCitySaved = savedWeather.any { it.city.equals(data.city, ignoreCase = true) }

                if (!isCitySaved) {
                    Button(
                        onClick = {
                            weatherViewModel.saveWeather(
                                city = data.city,
                                temp = data.temp,
                                wind = data.wind
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}