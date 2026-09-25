package com.phoenix.beetles.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.phoenix.beetles.feature.authors.navigation.AuthorsRoute
import com.phoenix.beetles.feature.authors.presentation.presenter.AuthorsViewModel
import com.phoenix.beetles.feature.authors.presentation.ui.AuthorsScreen
import com.phoenix.beetles.feature.registration.navigation.RegistrationRoute
import com.phoenix.beetles.feature.registration.presentation.presenter.RegistrationViewModel
import com.phoenix.beetles.feature.registration.presentation.ui.RegistrationScreen
import com.phoenix.beetles.feature.rules.navigation.RulesRoute
import com.phoenix.beetles.feature.rules.presentation.presenter.RulesViewModel
import com.phoenix.beetles.feature.rules.presentation.ui.RulesScreen
import com.phoenix.beetles.feature.settings.navigation.SettingRoule
import com.phoenix.beetles.feature.settings.presentation.presenter.SettingsViewModel
import com.phoenix.beetles.feature.settings.presentation.ui.SettingsScreen
import com.phoenix.beetles.navigation.NavigationOptions
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val tabs = NavigationOptions.entries

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(tab.routeClass) } == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(tab.routeObject) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RegistrationRoute,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable<RegistrationRoute> {
                val viewModel: RegistrationViewModel = koinViewModel()

                RegistrationScreen(
                    registrationViewModel = viewModel
                )
            }

            composable<RulesRoute> {
                val rulesViewModel: RulesViewModel = koinViewModel()

                RulesScreen(
                    rules = rulesViewModel.getRules()
                )
            }

            composable<AuthorsRoute> {
                val authorsViewModel: AuthorsViewModel = koinViewModel()

                AuthorsScreen(
                    viewModel = authorsViewModel
                )
            }

            composable<SettingRoule> {
                val settingsViewModel: SettingsViewModel = koinViewModel()

                SettingsScreen(
                    viewModel = settingsViewModel
                )
            }
        }
    }
}