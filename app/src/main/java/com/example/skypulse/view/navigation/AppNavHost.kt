package com.example.skypulse.view.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skypulse.view.home.HomeScreen

@Composable
fun AppNavHost() {

    val navController = rememberNavController()

  NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen()
        }

    }
}