# WeatherNotification App

A simple Android app to check the weather, built with modern Android technologies.

---

## Project Idea

The application allows users to search for the current weather in any city, save favorite locations locally, and manage the saved list. The app supports light and dark themes and works with real-time weather data from an external API.

---

## How the App Works

The user enters a city name and sends a request to the OpenWeather API. The app receives the current weather data and displays it on the screen. The user can save the city to a local database. All saved cities are displayed in a separate screen where the user can update or delete the saved data. The data is stored locally using Room and is preserved after restarting the application.

---

## Architecture

The application is built using the MVVM + Repository architecture.

Layers:
- UI: Jetpack Compose screens
- ViewModel: Business logic and state management
- Repository: Data operations (API + Room)
- Data layer: Room database, DAO, Retrofit API

---

## Tech Stack

- Language: Kotlin  
- UI: Jetpack Compose with Material 3  
- Asynchronous Work: Coroutines and Flow  
- Database: Room  
- Networking: Retrofit  
- Navigation: Navigation Compose  

---

## User Flow

1. User opens the app.
2. Enters a city name.
3. The app displays the current weather.
4. The user can save the city.
5. The user can navigate to the saved cities screen.
6. The user can update or delete saved cities.
7. Data is restored after app restart.

---

## Setup Instructions

1. Clone the repository:
   ```bash
      git clone [<your-repository-url>](https://github.com/tattianDrag64/AndroidAVD/WeatherNotification)
2. Open the project in Android Studio.
Add API Key:
Open the WeatherRepository.kt file and replace the API key with your own OpenWeather API key.
3. Run the application on an emulator or physical device.

---

## Tests

The project includes both Unit and UI tests.
- Unit Tests: Located in /app/src/test
- UI Tests (Espresso): Located in /app/src/androidTest

---

## APK

The APK file is located in the following path:
 1. Clone the repository:
   ```bash
      git clone [<your-repository-url>](https://github.com/tattianDrag64/AndroidAVD/WeatherNotification)
---

## Screenshots

