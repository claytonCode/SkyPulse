package com.example.skypulse.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.skypulse.R

@Composable
fun AppError.asString(): String = when (this) {

    AppError.NoInternet ->
        stringResource(R.string.error_no_internet)

    AppError.CityNotFound ->
        stringResource(R.string.error_city_not_found)

    AppError.LocationUnavailable ->
        stringResource(R.string.error_location)

    AppError.LocationNotFound ->
        stringResource(R.string.error_location_not_found)

    AppError.TooManyRequests ->
        stringResource(R.string.error_too_many_requests)

    AppError.Unauthorized ->
        stringResource(R.string.error_unknown)

    AppError.ServerError ->
        stringResource(R.string.error_server)

    AppError.Unknown ->
        stringResource(R.string.error_unknown)

}