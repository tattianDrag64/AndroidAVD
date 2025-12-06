package com.example.weathernotification.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weathernotification.data.DatabaseProvider
import com.example.weathernotification.ui.screens.SavedWeatherScreen
import com.example.weathernotification.ui.screens.WeatherScreen
import com.example.weathernotification.viewmodel.ThemeViewModel
import com.example.weathernotification.viewmodel.WeatherViewModel
import com.example.weathernotification.viewmodel.WeatherViewModelFactory

@Composable
fun AppNavHost(
    themeViewModel: ThemeViewModel
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val repository = DatabaseProvider.provideWeatherRepository(context)
    val weatherViewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(repository)
    )

    NavHost(navController = navController, startDestination = "weather") {
        composable(
            route = "weather?city={city}",
            arguments = listOf(navArgument("city") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val city = backStackEntry.arguments?.getString("city")
            val savedWeatherList by weatherViewModel.savedWeather.collectAsState()
            val weather = savedWeatherList.find { it.city == city }

            WeatherScreen(
                weatherViewModel = weatherViewModel,
                themeViewModel = themeViewModel,
                navController = navController,
                defaultCity = city ?: "",
                weatherItem = weather,
                onBack = if (navController.previousBackStackEntry != null) {
                    {
                        navController.popBackStack()
                    }
                } else {
                    null
                }
            )
        }
        composable("saved") {
            SavedWeatherScreen(viewModel = weatherViewModel, navController = navController)
        }
    }
}
