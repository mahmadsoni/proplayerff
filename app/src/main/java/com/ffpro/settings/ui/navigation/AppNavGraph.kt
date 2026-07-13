package com.ffpro.settings.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.padding
import com.ffpro.settings.i18n.LocalStrings
import com.ffpro.settings.presentation.viewmodel.MainViewModel
import com.ffpro.settings.ui.screens.GraphicsScreen
import com.ffpro.settings.ui.screens.HomeScreen
import com.ffpro.settings.ui.screens.HudScreen
import com.ffpro.settings.ui.screens.SensitivityScreen
import com.ffpro.settings.ui.screens.SettingsScreen

private sealed class Destination(val route: String) {
    data object Home : Destination("home")
    data object Sensitivity : Destination("sensitivity")
    data object Graphics : Destination("graphics")
    data object Hud : Destination("hud")
    data object Settings : Destination("settings")
}

@Composable
fun AppNavGraph(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val strings = LocalStrings.current

    Scaffold(
        bottomBar = {
            NavigationBar {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination

                val items = listOf(
                    Triple(Destination.Home, strings.navHome, Icons.Filled.Smartphone),
                    Triple(Destination.Sensitivity, strings.navSensitivity, Icons.Filled.TouchApp),
                    Triple(Destination.Graphics, strings.navGraphics, Icons.Filled.Speed),
                    Triple(Destination.Hud, strings.navHud, Icons.Filled.GridView),
                    Triple(Destination.Settings, strings.navSettings, Icons.Filled.Settings)
                )

                items.forEach { (destination, label, icon) ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Destination.Home.route) { HomeScreen(viewModel) }
            composable(Destination.Sensitivity.route) { SensitivityScreen(viewModel) }
            composable(Destination.Graphics.route) { GraphicsScreen(viewModel) }
            composable(Destination.Hud.route) { HudScreen(viewModel) }
            composable(Destination.Settings.route) { SettingsScreen(viewModel) }
        }
    }
}
