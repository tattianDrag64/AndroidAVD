package com.example.weathernotification

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weathernotification.data.database.DatabaseProvider
import com.example.weathernotification.data.repository.ThemeRepository
import com.example.weathernotification.presentation.NavGraph
import com.example.weathernotification.ui.theme.WeatherNotificationTheme
import com.example.weathernotification.viewmodel.Theme
import com.example.weathernotification.viewmodel.ThemeViewModel
import com.example.weathernotification.viewmodel.ThemeViewModelFactory
import com.example.weathernotification.viewmodel.WeatherViewModel
import com.example.weathernotification.viewmodel.WeatherViewModelFactory

//main activity of the application, entry point
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //initializing theme repository and viewModel
            val themeRepository = ThemeRepository(LocalContext.current)
            val themeViewModel: ThemeViewModel = viewModel(factory = ThemeViewModelFactory(themeRepository))
            val theme by themeViewModel.theme.collectAsState()

            //applying the selected theme
            WeatherNotificationTheme(
                darkTheme = theme == Theme.DARK
            ) {
                //providing weather repository and viewModel
                val repository = DatabaseProvider.provideWeatherRepository(LocalContext.current)
                val weatherViewModel: WeatherViewModel = viewModel(
                    factory = WeatherViewModelFactory(repository)
                )
                //setting up the navigation graph
                NavGraph(
                    weatherViewModel = weatherViewModel,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}
