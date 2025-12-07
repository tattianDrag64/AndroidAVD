package com.example.weathernotification.viewmodel

import android.util.Log
import app.cash.turbine.test
import com.example.weathernotification.data.remote.model.Main
import com.example.weathernotification.data.remote.model.WeatherDescription
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
        // given
        val city = "London"
        val response = WeatherResponse(
            name = "London",
            main = Main(temp = 15.0, humidity = 80),
            wind = Wind(speed = 5.0),
            weather = listOf(WeatherDescription(description = "clear sky"))
        )
        coEvery { repository.loadWeatherFromApi(city) } returns response

        //when
        viewModel.loadWeather(city)

        //then
        viewModel.weather.test {
            val item = awaitItem()
            assertNotNull(item)

            assertEquals("London", item!!.city)
            assertEquals(15.0, item.temp, 0.0)
            assertEquals(5.0, item.wind, 0.0)
            assertEquals("clear sky", item.description)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `saveWeather should call repository`() = runTest {
        //given
        val city = "Paris"
        val temp = 20.0
        val wind = 3.0
        val description = "cloudy"
        coEvery { repository.saveWeatherToDb(any()) } returns Unit

        //when
        viewModel.saveWeather(city, temp, wind, description)

        // then
        coVerify { repository.saveWeatherToDb(any()) }
    }

    @Test
    fun `loadWeather should update error state on error`() = runTest {
        // given
        val city = "InvalidCity"
        val exception = RuntimeException("City not found")
        coEvery { repository.loadWeatherFromApi(city) } throws exception

        //when
        viewModel.loadWeather(city)

        //then
        viewModel.weather.test {
            assertEquals(null, awaitItem())
            expectNoEvents()
        }

        viewModel.error.test {
            assertEquals("City not found or invalid name", awaitItem())
        }
    }
}