package com.example.weathernotification.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class Theme {
    LIGHT,
    DARK
}

class ThemeViewModel : ViewModel() {
    private val _theme = MutableStateFlow(Theme.LIGHT) // Start with light theme
    val theme = _theme.asStateFlow()

    fun toggleTheme() {
        _theme.value = when (_theme.value) {
            Theme.LIGHT -> Theme.DARK
            Theme.DARK -> Theme.LIGHT
        }
    }
}