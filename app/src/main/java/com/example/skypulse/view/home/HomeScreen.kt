package com.example.skypulse.view.home

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.skypulse.view.components.SearchBar
import com.example.skypulse.view.components.WeatherCard
import com.example.skypulse.viewmodel.HomeViewModel
import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.skypulse.R
import com.example.skypulse.ui.theme.SkyBlue
import com.example.skypulse.util.asString
import com.example.skypulse.view.components.ForecastRow
import com.example.skypulse.view.events.HomeUiEvent

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        context.getString(event.messageRes)
                    )
                }
            }
        }
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                viewModel.loadCurrentLocationWeather()
            }
        }

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {

                viewModel.loadCurrentLocationWeather()
            }

            else -> {
                permissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!uiState.isLoading) {
                        viewModel.loadCurrentLocationWeather()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = stringResource(R.string.refresh_weather)
                )

            }
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 28.dp, vertical = 2.dp),

            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                    color = SkyBlue
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            SearchBar(
                city = uiState.city,
                isLoading = uiState.isLoading,
                onCityChange = viewModel::onCityChange,
                onSearchClick = viewModel::searchWeather,
                suggestions = uiState.suggestions,
            )

            Spacer(modifier = Modifier.height(24.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 3.dp,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.loading_weather),
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary
                            )

                        }

                    }
                }

                uiState.error != null -> {
                    uiState.error?.let { error ->
                        Text(text = error.asString())
                    }
                }

                uiState.weather != null -> {
                    AnimatedContent(
                        targetState = uiState.weather,
                        label = stringResource(R.string.weather_content)
                    ) { weather ->
                        weather?.let {

                            Column {
                                WeatherCard(
                                    weather = it
                                )

                                Spacer(
                                    modifier = Modifier.height(12.dp)
                                )

                                AnimatedVisibility(
                                    visible = uiState.dailyForecast.isNotEmpty(),
                                    enter = fadeIn() + slideInVertically(),
                                    exit = fadeOut()
                                ) {

                                    ForecastRow(
                                        forecast = uiState.dailyForecast
                                    )

                                }

                            }

                        }

                    }

                }

            }

        }
    }
}