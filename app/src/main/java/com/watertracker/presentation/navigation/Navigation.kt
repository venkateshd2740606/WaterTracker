package com.watertracker.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.watertracker.ads.AdManager
import com.watertracker.analytics.AnalyticsManager
import com.watertracker.domain.model.UserPreferences
import com.watertracker.presentation.ui.screens.history.HistoryScreen
import com.watertracker.presentation.ui.screens.log.LogScreen
import com.watertracker.presentation.ui.screens.settings.SettingsScreen
import com.watertracker.presentation.ui.screens.today.TodayScreen

sealed class Screen(val route: String) {
    data object Today : Screen("today")
    data object Log : Screen("log")
    data object History : Screen("history")
    data object Settings : Screen("settings")
}

@Composable
fun WaterTrackerNavHost(
    navController: NavHostController,
    adManager: AdManager,
    analyticsManager: AnalyticsManager,
    preferences: UserPreferences,
    startDestination: String = Screen.Today.route
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(
                    Triple(Screen.Today.route, Icons.Default.Home, "Today"),
                    Triple(Screen.Log.route, Icons.Default.LocalDrink, "Log"),
                    Triple(Screen.History.route, Icons.Default.History, "History"),
                    Triple(Screen.Settings.route, Icons.Default.Settings, "Settings")
                ).forEach { (route, icon, label) ->
                    NavigationBarItem(
                        selected = currentRoute == route,
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(icon, null) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController, startDestination, Modifier.padding(padding)) {
            composable(Screen.Today.route) { TodayScreen(adManager, preferences.adsEnabled) }
            composable(Screen.Log.route) { LogScreen() }
            composable(Screen.History.route) { HistoryScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
        }
    }
}
