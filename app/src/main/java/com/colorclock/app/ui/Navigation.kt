package com.colorclock.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.colorclock.app.ClockViewModel

@Composable
fun AppNavigation(viewModel: ClockViewModel) {
    val navController = rememberNavController()
    val clockState by viewModel.clockState.collectAsState()
    val settings by viewModel.settings.collectAsState()

    NavHost(navController = navController, startDestination = "clock") {
        composable("clock") {
            ClockScreen(
                clockState = clockState,
                settings = settings,
                onOpenSettings = { navController.navigate("settings") },
            )
        }
        composable("settings") {
            SettingsScreen(
                settings = settings,
                onSettingsChange = { viewModel.updateSettings(it) },
                onBack = { navController.popBackStack() },
            )
        }
    }
}
