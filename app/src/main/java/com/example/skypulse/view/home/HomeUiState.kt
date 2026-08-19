package com.example.skypulse.view.home

import com.example.skypulse.data.model.DailyForecast
import com.example.skypulse.data.model.WeatherResponse
import com.example.skypulse.data.model.forecast.ForecastResponse
import com.example.skypulse.location.LocationSuggestion
import com.example.skypulse.util.AppError

data class HomeUiState(
    val city: String = "",
    val weather: WeatherResponse? = null,
    val dailyForecast: List<DailyForecast> = emptyList(),
    val isLoading: Boolean = false,
    val isGettingLocation: Boolean = false,
    val suggestions: List<LocationSuggestion> = emptyList(),
    val error: AppError? = null
)