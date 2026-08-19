package com.example.skypulse.util

sealed class AppError {

    data object NoInternet : AppError()
    data object CityNotFound : AppError()
    data object ServerError : AppError()
    data object Unauthorized : AppError()
    data object Unknown : AppError()
    data object LocationNotFound : AppError()
    data object TooManyRequests : AppError()
    data object LocationUnavailable : AppError()

}