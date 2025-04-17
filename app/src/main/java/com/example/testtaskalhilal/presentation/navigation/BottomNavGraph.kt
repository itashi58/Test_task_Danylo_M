package com.example.testtaskalhilal.presentation.navigation


import HomeScreen
import RatesScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.createGraph
import com.example.testtaskalhilal.R
import com.example.testtaskalhilal.ui.theme.ColorPrimary
import com.example.testtaskalhilal.ui.theme.White
import kotlin.collections.forEach

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    val graph =
        navController.createGraph(startDestination = Screen.Home.rout) {
            composable(route = Screen.Home.rout) {
                HomeScreen()
            }
            composable(route = Screen.Rates.rout) {
                RatesScreen()
            }
        }
    NavHost(
        navController = navController,
        graph = graph,
        modifier = Modifier.padding(innerPadding)
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.black)
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        screensList.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = ColorPrimary,
                    selectedTextColor = ColorPrimary,
                    unselectedIconColor = White,
                    unselectedTextColor = White
                )
            )
        }
    }
}