package com.example.skypulse.data.model

data class DailyForecast(
    val day: String,
    val minTemp: Double,
    val maxTemp: Double,
    val icon: String,
    val description: String
)