package com.example.skypulse.data.repository

import com.example.skypulse.location.LocationSuggestion
import com.example.skypulse.util.Resource


interface GeoRepository {
    suspend fun searchCities(
        query: String
    ): Resource<List<LocationSuggestion>>

}