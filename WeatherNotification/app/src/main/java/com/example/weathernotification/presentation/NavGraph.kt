package com.example.weathernotification.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weathernotification.ui.screens.SavedWeatherScreen
import com.example.weathernotification.ui.screens.WeatherScreen
import com.example.weathernotification.viewmodel.ThemeViewModel
import com.example.weathernotification.viewmodel.WeatherViewModel

@Composable
fun NavGraph(
    weatherViewModel: WeatherViewModel,
    themeViewModel: ThemeViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "weather" //main screen - weather search
    ) {

        //screen with the list of saved cities
        composable("saved") {
            SavedWeatherScreen(
                viewModel = weatherViewModel, //viewModel is the correct parameter name
                navController = navController
            )
        }

        //weather screen, can take an optional city argument
        composable(
            route = "weather?city={city}",
            arguments = listOf(
                navArgument("city") {
                    type = NavType.StringType
                    nullable = true //city can be absent on the first launch
                }
            )
        ) { backStackEntry ->
            //getting city from arguments
            val city = backStackEntry.arguments?.getString("city")
            //getting the list of saved weather
            val savedWeatherList by weatherViewModel.savedWeather.collectAsState()
            //finding the specific weather item for the city
            val weather = savedWeatherList.find { it.city == city }

            WeatherScreen(
                weatherViewModel = weatherViewModel,
                themeViewModel = themeViewModel, //passing the themeViewModel
                navController = navController,
                defaultCity = city ?: "",
                weatherItem = weather, //passing the found city for the "update" button logic
                onBack = { navController.popBackStack() }
            )
        }
    }
}
