package com.example.skypulse.mapper


import com.example.skypulse.data.model.DailyForecast
import com.example.skypulse.data.model.forecast.Item
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun List<Item>.toDailyForecast(): List<DailyForecast> {

    val inputFormat = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    )

    val outputFormat = SimpleDateFormat(
        "EEE",
        Locale.getDefault()
    )

    return groupBy {
        it.dt_txt.substringBefore(" ")
    }.entries
        .drop(1)
        .take(5)
        .map { (_, forecasts) ->

            val representative = forecasts.minByOrNull {

                kotlin.math.abs(
                    it.dt_txt.substring(11, 13).toInt() - 12
                )

            } ?: forecasts.first()

            DailyForecast(

                day = outputFormat.format(
                    inputFormat.parse(representative.dt_txt) ?: Date()
                ),

                minTemp = forecasts.minOf {
                    it.main.temp_min
                },

                maxTemp = forecasts.maxOf {
                    it.main.temp_max
                },

                icon = representative.weather.first().icon,

                description = representative.weather.first().description

            )

        }

}
