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
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    //collecting state from viewModels
    val weather by weatherViewModel.weather.collectAsState()
    val savedWeather by weatherViewModel.savedWeather.collectAsState()
    val error by weatherViewModel.error.collectAsState()
    var city by remember { mutableStateOf(defaultCity) }

    //theme state
    val currentTheme by themeViewModel.theme.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    //effect to load weather data when the screen is opened with a city
    LaunchedEffect(defaultCity, weatherItem) {
        if (weatherItem != null) {
            //if a specific weather item is passed, show its data
            weatherViewModel.setWeather(weatherItem)
        } else if (defaultCity.isNotBlank()) {
            //if a city name is passed, load its weather
            weatherViewModel.loadWeather(defaultCity)
        } else {
            //clear the text field if we just entered the screen
            city = ""
        }
    }

    //effect to show a snackbar when an error occurs
    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            weatherViewModel.clearError() //clear the error so it doesn't show again
        }
    }

    //clear the state when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            weatherViewModel.clearWeather()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Weather") },
                actions = {
                    //button to toggle the theme
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

            //textfield for city input
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Insert city") },
                modifier = Modifier.fillMaxWidth()
            )

            //button to trigger weather loading
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

            //button to navigate to the list of saved locations
            Button(
                onClick = {
                    navController.navigate("saved")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Saved Locations")
            }

            Spacer(modifier = Modifier.height(16.dp))

            //this block is shown only when weather data is available
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
                            text = "Description: ${data.description}"
                        )

                        Text(
                            text = "Temperature: ${data.temp} °C"
                        )

                        Text(
                            text = "Wind: ${data.wind} m/s"
                        )
                    }
                }

                //check if the current city is already saved
                val isCitySaved = savedWeather.any { it.city.equals(data.city, ignoreCase = true) }

                //show the save button only if the city is not saved yet
                if (!isCitySaved) {
                    Button(
                        onClick = {
                            weatherViewModel.saveWeather(
                                city = data.city,
                                temp = data.temp,
                                wind = data.wind,
                                description = data.description
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