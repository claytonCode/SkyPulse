package com.example.skypulse.data.repository

import com.example.skypulse.BuildConfig
import com.example.skypulse.data.remote.GeoApi
import com.example.skypulse.location.LocationSuggestion
import com.example.skypulse.util.AppError
import com.example.skypulse.util.Resource
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class GeoRepositoryImp @Inject constructor(
    private val geoApi: GeoApi
) : GeoRepository {

    override suspend fun searchCities(
        query: String
    ): Resource<List<LocationSuggestion>> {

        return try {

            val locations = geoApi.searchCities(
                query = query,
                apiKey = BuildConfig.API_KEY
            )

            Resource.Success(locations)

        } catch (e: UnknownHostException) {

            Resource.Error(AppError.NoInternet)

        } catch (e: HttpException) {

            Resource.Error(
                when (e.code()) {

                    401 -> AppError.Unauthorized

                    500, 502, 503 -> AppError.ServerError

                    else -> AppError.Unknown

                }
            )

        } catch (e: Exception) {

            Resource.Error(AppError.Unknown)

        }

    }

}