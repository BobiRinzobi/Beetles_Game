package com.phoenix.beetles.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.phoenix.beetles.feature.authors.presentation.presenter.AuthorsViewModel
import com.phoenix.beetles.feature.authors.presentation.ui.AuthorsScreen
import com.phoenix.beetles.feature.core.presentation.presenter.GameViewModel
import com.phoenix.beetles.feature.core.presentation.ui.GameScreen
import com.phoenix.beetles.feature.registration.presentation.presenter.RegistrationViewModel
import com.phoenix.beetles.feature.registration.presentation.ui.RegistrationScreen
import com.phoenix.beetles.feature.rules.presentation.presenter.RulesViewModel
import com.phoenix.beetles.feature.rules.presentation.ui.RulesScreen
import com.phoenix.beetles.feature.settings.presentation.presenter.SettingsViewModel
import com.phoenix.beetles.feature.settings.presentation.ui.SettingsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun BeetlesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Registration,
        modifier = modifier,
    ) {
        composable<AppRoute.Registration> {
            val viewModel: RegistrationViewModel = koinViewModel()
            RegistrationScreen(registrationViewModel = viewModel)
        }
        composable<AppRoute.Rules> {
            val viewModel: RulesViewModel = koinViewModel()
            RulesScreen(rules = viewModel.getRules())
        }
        composable<AppRoute.Authors> {
            val viewModel: AuthorsViewModel = koinViewModel()
            AuthorsScreen(viewModel = viewModel)
        }
        composable<AppRoute.Settings> {
            val viewModel: SettingsViewModel = koinViewModel()
            SettingsScreen(viewModel = viewModel)
        }
        composable<AppRoute.Game> {
            val viewModel: GameViewModel = koinViewModel()
            GameScreen(viewModel = viewModel)
        }
    }
}
