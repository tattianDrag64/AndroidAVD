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
        startDestination = "weather" // Главный экран - поиск погоды
    ) {

        // Экран со списком сохраненных городов
        composable("saved") {
            SavedWeatherScreen(
                viewModel = weatherViewModel, // viewModel - правильное имя параметра
                navController = navController
            )
        }

        // Экран погоды, может принимать опциональный аргумент city
        composable(
            route = "weather?city={city}",
            arguments = listOf(
                navArgument("city") {
                    type = NavType.StringType
                    nullable = true // Город может отсутствовать при первом запуске
                }
            )
        ) { backStackEntry ->
            val city = backStackEntry.arguments?.getString("city")
            val savedWeatherList by weatherViewModel.savedWeather.collectAsState()
            val weather = savedWeatherList.find { it.city == city }

            WeatherScreen(
                weatherViewModel = weatherViewModel,
                themeViewModel = themeViewModel, // Передаем themeViewModel
                navController = navController,
                defaultCity = city ?: "",
                weatherItem = weather, // Передаем найденный город для логики кнопки "Обновить"
                onBack = { navController.popBackStack() }
            )
        }
    }
}
