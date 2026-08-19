package com.example.skypulse.data.remote

import com.example.skypulse.data.model.WeatherResponse
import com.example.skypulse.data.model.forecast.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(

        @Query("q")
        city: String,

        @Query("appid")
        apiKey: String,

        @Query("units")
        units: String = "metric"
    ): WeatherResponse

    @GET("weather")
    suspend fun getWeather(
        @Query("lat")
        latitude: Double,

        @Query("lon")
        longitude: Double,

        @Query("appid")
        apiKey: String,

        @Query("units")
        units: String = "metric"
    ): WeatherResponse

    @GET("forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): ForecastResponse

    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): ForecastResponse

}