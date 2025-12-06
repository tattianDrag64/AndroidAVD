package com.example.weathernotification

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weathernotification.data.DatabaseProvider
import com.example.weathernotification.ui.screens.WeatherScreen
import com.example.weathernotification.ui.theme.WeatherNotificationTheme
import com.example.weathernotification.viewmodel.WeatherViewModel
import com.example.weathernotification.viewmodel.WeatherViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val repository = DatabaseProvider.provideWeatherRepository(this)
            val viewModel: WeatherViewModel = viewModel(
                factory = WeatherViewModelFactory(repository)
            )

            WeatherNotificationTheme {
                WeatherScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherNotificationTheme {
        Greeting("Android")
    }
}
