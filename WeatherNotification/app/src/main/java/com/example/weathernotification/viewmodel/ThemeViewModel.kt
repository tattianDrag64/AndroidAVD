package com.example.weathernotification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathernotification.data.repository.ThemeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Theme {
    LIGHT,
    DARK
}

class ThemeViewModel(private val repository: ThemeRepository) : ViewModel() {

    val theme: StateFlow<Theme> = repository.isDarkTheme
        .map { isDarkTheme ->
            if (isDarkTheme) Theme.DARK else Theme.LIGHT
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Theme.LIGHT)

    fun toggleTheme() {
        viewModelScope.launch {
            val isDark = theme.value == Theme.DARK
            repository.setTheme(!isDark)
        }
    }
}