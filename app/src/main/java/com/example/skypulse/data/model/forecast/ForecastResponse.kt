package com.example.skypulse.data.model.forecast

data class ForecastResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Item>,
    val message: Int
)