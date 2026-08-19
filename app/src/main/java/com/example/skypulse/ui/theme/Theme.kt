package com.example.skypulse.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = SkyBlue,
    background = Color(0xFFE3F2FD),
    surface = Color.White,
    surfaceContainerHigh = Color(0xFFE3F2FD)
)

private val DarkColors = darkColorScheme(
    primary = SkyBlue,
    background = Color(0xFF23374D),
    surface = Color(0xFF23374D),
    surfaceContainerHigh = Color(0xFF23374D)
)
@Composable
fun SkyPulseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )

}