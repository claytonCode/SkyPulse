package com.example.skypulse.location

data class LocationSuggestion(
    val name: String,
    val country: String,
    val state: String?,
    val lat: Double,
    val lon: Double
)