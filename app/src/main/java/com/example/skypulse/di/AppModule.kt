package com.example.skypulse.di

import android.content.Context
import com.example.skypulse.data.remote.GeoApi
import com.example.skypulse.data.remote.WeatherApi
import com.example.skypulse.data.repository.GeoRepository
import com.example.skypulse.data.repository.GeoRepositoryImp
import com.example.skypulse.data.repository.WeatherRepository
import com.example.skypulse.data.repository.WeatherRepositoryImpl
import com.example.skypulse.location.DefaultLocationTracker
import com.example.skypulse.location.LocationTracker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("GeoRetrofit")
    fun provideGeoRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideWeatherApi(
        retrofit: Retrofit
    ): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGeoApi(
        @Named("GeoRetrofit") retrofit: Retrofit
    ): GeoApi {
        return retrofit.create(GeoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationClient(
        @ApplicationContext context: Context
    ) = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideLocationTracker(
        locationClient: FusedLocationProviderClient,
        @ApplicationContext context: Context
    ): LocationTracker {

        return DefaultLocationTracker(
            locationClient,
            context
        )
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class RepositoryModule {

        @Binds
        @Singleton
        abstract fun bindRepository(
            repository: WeatherRepositoryImpl
        ): WeatherRepository
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class GeoRepositoryModule {

        @Binds
        @Singleton
        abstract fun bindRepository(
            repository: GeoRepositoryImp
        ): GeoRepository
    }
}