package com.example.skypulse.data.repository

import com.example.skypulse.BuildConfig
import com.example.skypulse.data.model.WeatherResponse
import com.example.skypulse.data.model.forecast.ForecastResponse
import com.example.skypulse.data.remote.WeatherApi
import com.example.skypulse.util.AppError
import com.example.skypulse.util.Resource
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {

    override suspend fun getWeather(
        city: String
    ): Resource<WeatherResponse> {

        return try {
            val response = weatherApi.getWeather(
                city = city,
                apiKey = BuildConfig.API_KEY
            )

            Resource.Success(response)

        } catch (e: UnknownHostException) {
            Resource.Error(AppError.NoInternet)

        } catch (e: HttpException) {
            val error = when (e.code()) {
                404 -> AppError.CityNotFound
                401 -> AppError.Unauthorized
                500, 502, 503 -> AppError.ServerError

                else -> AppError.Unknown
            }

            Resource.Error(error)
        } catch (e: Exception) {
            Resource.Error(AppError.Unknown)
        }

    }

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): Resource<WeatherResponse> {

        return try {
            val response = weatherApi.getWeather(
                latitude = latitude,
                longitude = longitude,
                apiKey = BuildConfig.API_KEY
            )

            Resource.Success(response)

        } catch (e: UnknownHostException) {
            Resource.Error(AppError.NoInternet)

        } catch (e: HttpException) {

            Resource.Error(
                when (e.code()) {

                    404 -> AppError.LocationUnavailable

                    401 -> AppError.Unauthorized

                    500, 502, 503 -> AppError.ServerError

                    else -> AppError.Unknown

                }
            )

        } catch (e: Exception) {

            Resource.Error(AppError.Unknown)

        }

    }

    override suspend fun getForecast(
        city:
        String
    ): Resource<ForecastResponse> {

        return try {
            val response = weatherApi.getForecast(
                city = city,
                apiKey = BuildConfig.API_KEY
            )

            Resource.Success(response)

        } catch (e: UnknownHostException) {
            Resource.Error(AppError.NoInternet)

        } catch (e: HttpException) {

            val error = when (e.code()) {
                404 -> AppError.CityNotFound
                401 -> AppError.Unauthorized
                429 -> AppError.TooManyRequests
                500, 502, 503 -> AppError.ServerError
                else -> AppError.Unknown
            }

            Resource.Error(error)

        } catch (e: Exception) {
            Resource.Error(AppError.Unknown)
        }
    }

    override suspend fun getForecast(
        latitude: Double,
        longitude: Double
    ): Resource<ForecastResponse> {
        return try {

            val response = weatherApi.getForecast(
                latitude = latitude,
                longitude = longitude,
                apiKey = BuildConfig.API_KEY
            )

            Resource.Success(response)

        } catch (e: UnknownHostException) {

            Resource.Error(AppError.NoInternet)

        } catch (e: HttpException) {

            val error = when (e.code()) {
                404 -> AppError.LocationNotFound
                401 -> AppError.Unauthorized
                429 -> AppError.TooManyRequests
                500, 502, 503 -> AppError.ServerError

                else -> AppError.Unknown

            }

            Resource.Error(error)

        } catch (e: Exception) {
            Resource.Error(AppError.Unknown)

        }

    }

}