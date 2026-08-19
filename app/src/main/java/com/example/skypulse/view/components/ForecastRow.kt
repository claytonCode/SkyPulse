package com.example.skypulse.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.skypulse.data.model.DailyForecast

@Composable
fun ForecastRow(
    forecast: List<DailyForecast>
) {

    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(forecast) {
            ForecastCard(
                forecast = it
            )

        }
    }
}