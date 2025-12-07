package com.example.weathernotification.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weathernotification.data.entity.WeatherEntity
import com.example.weathernotification.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedWeatherScreen(
    viewModel: WeatherViewModel,
    navController: NavController
) {
    //collecting the list of saved weather from the viewModel
    val list by viewModel.savedWeather.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Cities") },
                navigationIcon = {
                    //back button to return to the previous screen
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        //displaying a list of items
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(list) { weather ->
                WeatherItem(
                    weather = weather,
                    onClick = {
                        //set the weather in the viewModel and navigate to the main screen
                        viewModel.setWeather(weather)
                        navController.navigate("weather?city=${weather.city}")
                    },
                    onDelete = {
                        //delete the item from the database
                        viewModel.deleteWeather(weather)
                    },
                    onUpdate = {
                        //update the weather data for this city
                        viewModel.updateWeather(weather)
                    }
                )
            }
        }
    }
}

@Composable
fun WeatherItem(
    weather: WeatherEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() } //handle clicks on the card
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("City: ${weather.city}")
            Text("Temperature: ${weather.temp}°C")
            Text("Wind: ${weather.wind} m/s")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround //place buttons with space around
            ) {
                Button(onClick = onUpdate) {
                    Text("Update")
                }
                Button(onClick = onDelete) {
                    Text("Delete")
                }
            }
        }
    }
}