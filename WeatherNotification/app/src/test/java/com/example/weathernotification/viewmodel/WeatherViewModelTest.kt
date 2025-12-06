package com.example.weathernotification.viewmodel

import app.cash.turbine.test
import com.example.weathernotification.data.entity.WeatherEntity
import com.example.weathernotification.data.remote.model.Main
import com.example.weathernotification.data.remote.model.WeatherResponse
import com.example.weathernotification.data.remote.model.Wind
import com.example.weathernotification.data.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: WeatherRepository
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = WeatherViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadWeather should update weather state on success`() = runTest {
        // Given
        val city = "London"
        val response = WeatherResponse(
            name = "London",
            main = Main(temp = 15.0, humidity = 80),
            wind = Wind(speed = 5.0),
            weather = emptyList()
        )
        coEvery { repository.loadWeatherFromApi(city) } returns response
        coEvery { repository.getSavedWeather() } returns kotlinx.coroutines.flow.flowOf(emptyList())

        // When
        viewModel.loadWeather(city)

        // Then
        viewModel.weather.test {
            val item = awaitItem()
            assertNotNull(item)
            
            // Используем `!!` на каждой строке
            assertEquals("London", item!!.city)
            assertEquals(15.0, item.temp, 0.0)
            assertEquals(5.0, item.wind, 0.0)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `saveWeather should call repository`() = runTest {
        // Given
        val city = "Paris"
        val temp = 20.0
        val wind = 3.0
        coEvery { repository.saveWeatherToDb(any()) } returns Unit

        // When
        viewModel.saveWeather(city, temp, wind)

        // Then
        coVerify { repository.saveWeatherToDb(any()) }
    }

    @Test
    fun `loadWeather should not update weather state on error`() = runTest {
        // Given
        val city = "InvalidCity"
        val exception = RuntimeException("City not found")
        coEvery { repository.loadWeatherFromApi(city) } throws exception
        coEvery { repository.getSavedWeather() } returns kotlinx.coroutines.flow.flowOf(emptyList())

        // When
        viewModel.loadWeather(city)

        // Then
        viewModel.weather.test {
            assertEquals(null, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
}