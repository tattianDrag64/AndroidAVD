package com.example.weathernotification

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weathernotification.ui.theme.WeatherNotificationTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherScreenTest {

    //this rule launches the MainActivity
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun searchForCity_displaysWeatherCard() {
        //find the text field by its label text
        composeTestRule.onNodeWithText("Insert city").performTextInput("London")

        //find the button by its text and click it
        composeTestRule.onNodeWithText("Show weather").performClick()

        //wait for the api call and check if the card with the city name appears
        //we might need to wait a bit for the network call
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            //check if a node with "City: London" exists
            composeTestRule.onNodeWithText("City: London", substring = true).isDisplayed()
        }

        //check if the description text is present
        composeTestRule.onNodeWithText("Description:", substring = true).assertIsDisplayed()
    }
}