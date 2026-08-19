package com.example.skypulse.data.repository

import com.example.skypulse.data.model.WeatherResponse
import com.example.skypulse.data.model.forecast.ForecastResponse
import com.example.skypulse.util.Resource

interface WeatherRepository {

    suspend fun getWeather(
        city: String
    ): Resource<WeatherResponse>

    suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): Resource<WeatherResponse>

    suspend fun getForecast(
        city: String
    ): Resource<ForecastResponse>

    suspend fun getForecast(
        latitude: Double,
        longitude: Double
    ): Resource<ForecastResponse>
}
