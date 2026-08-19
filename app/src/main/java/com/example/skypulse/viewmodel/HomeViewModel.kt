package com.example.skypulse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skypulse.R
import com.example.skypulse.data.model.WeatherResponse
import com.example.skypulse.data.model.forecast.ForecastResponse
import com.example.skypulse.data.repository.GeoRepository
import com.example.skypulse.data.repository.WeatherRepository
import com.example.skypulse.location.LocationTracker
import com.example.skypulse.mapper.toDailyForecast
import com.example.skypulse.util.AppError
import com.example.skypulse.util.Resource
import com.example.skypulse.view.events.HomeUiEvent
import com.example.skypulse.view.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val geoRepository: GeoRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val cityQuery = MutableStateFlow("")

    private val _events = Channel<HomeUiEvent>()
    val events = _events.receiveAsFlow()

    init {

        viewModelScope.launch {
            cityQuery
                .debounce(500.milliseconds)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isBlank()) {
                        _uiState.update {
                            it.copy(
                                suggestions = emptyList()
                            )
                        }

                        return@collect
                    }
                    when (val result = geoRepository.searchCities(query)) {

                        is Resource.Success -> {
                            _uiState.update {
                                it.copy(
                                    suggestions = result.data ?: emptyList()
                                )
                            }
                        }

                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    suggestions = emptyList()
                                )
                            }
                        }

                    }
                }
        }

    }

    fun onCityChange(city: String) {
        _uiState.update {
            it.copy(city = city)
        }
        cityQuery.value = city
    }

    private suspend fun loadWeather(
        weatherCall: suspend () -> Resource<WeatherResponse>,
        forecastCall: suspend () -> Resource<ForecastResponse>
    ) = coroutineScope {

        val weatherDeferred = async {
            weatherCall()
        }

        val forecastDeferred = async {
            forecastCall()
        }

        val weatherResult = weatherDeferred.await()
        val forecastResult = forecastDeferred.await()

        when {
            weatherResult is Resource.Success &&
                    forecastResult is Resource.Success -> {
                _uiState.update {
                    it.copy(
                        weather = weatherResult.data,
                        dailyForecast = forecastResult.data
                            ?.list
                            ?.toDailyForecast()
                            ?: emptyList(),
                        isLoading = false,
                        isGettingLocation = false,
                        error = null
                    )
                }

            }

            weatherResult is Resource.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isGettingLocation = false,
                        error = weatherResult.error
                    )
                }

            }

            forecastResult is Resource.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isGettingLocation = false,
                        error = forecastResult.error
                    )
                }

            }

        }

    }
    fun searchWeather(city: String) {

        val city = _uiState.value.city.trim()

        if (city.isBlank()) {
            viewModelScope.launch {
                _events.send(
                    HomeUiEvent.ShowSnackbar(
                        R.string.error_empty_city
                    )
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            loadWeather(
                weatherCall = {
                    repository.getWeather(city)
                },

                forecastCall = {
                    repository.getForecast(city)
                }

            )

        }

    }
    fun loadCurrentLocationWeather() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isGettingLocation = true,
                    error = null
                )
            }

            val location = locationTracker.getCurrentLocation()

            if (location == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isGettingLocation = false,
                        error = AppError.LocationUnavailable
                    )
                }

                return@launch
            }

            loadWeather(
                weatherCall = {
                    repository.getWeather(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                },

                forecastCall = {
                    repository.getForecast(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                }

            )

        }

    }


}