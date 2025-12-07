package com.example.weathernotification.viewmodel

import android.util.Log
import app.cash.turbine.test
import com.example.weathernotification.data.remote.model.Main
import com.example.weathernotification.data.remote.model.WeatherResponse
import com.example.weathernotification.data.remote.model.Wind
import com.example.weathernotification.data.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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

    // Используем UnconfinedTestDispatcher для немедленного выполнения корутин
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: WeatherRepository
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()

        mockkStatic(Log::class)
        every { Log.e(any(), any<String>()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        every { repository.getSavedWeather() } returns flowOf(emptyList())
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

        // When
        viewModel.loadWeather(city)

        // Then
        viewModel.weather.test {
            // С UnconfinedTestDispatcher корутина выполняется сразу.
            // Поэтому мы не увидим начальное значение null, а сразу получим результат.
            val item = awaitItem()
            assertNotNull(item)

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

        // When
        viewModel.loadWeather(city)

        // Then
        viewModel.weather.test {
            assertEquals(null, awaitItem())
            // Убеждаемся, что других событий не было
            expectNoEvents()
        }
    }
}