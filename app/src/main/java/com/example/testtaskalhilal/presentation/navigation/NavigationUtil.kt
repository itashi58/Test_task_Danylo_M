package com.example.testtaskalhilal.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

internal val screensList = listOf(
    BottomNavItem("Home", Icons.Default.Home, Screen.Home.rout),
    BottomNavItem("Rates", Icons.AutoMirrored.Sharp.List, Screen.Rates.rout)
)

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

sealed class Screen(val rout: String) {
    object Home: Screen("home_screen")
    object Rates: Screen("rates_screen")
}