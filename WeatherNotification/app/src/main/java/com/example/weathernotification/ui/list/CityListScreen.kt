package com.example.weathernotification.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.weathernotification.viewmodel.CityViewModel

@Composable
fun CityListScreen(navController: NavController) {

    val viewModel: CityViewModel = viewModel()
    val cities by viewModel.cities.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.addCity("Sofia", 42.7, 23.3)
            }) {
                Text("+")
            }
        }
    ) { padding ->

        LazyColumn(modifier = Modifier.padding(padding)) {
            items(cities) { city ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            navController.navigate(
                                "weather/${city.latitude}/${city.longitude}/${city.name}"
                            )
                        }
                ) {
                    Text(
                        text = city.name,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
