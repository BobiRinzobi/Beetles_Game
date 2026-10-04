package com.phoenix.beetles.navigation

import androidx.compose.runtime.Composable
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
fun NavigationPanel(route: AppRoute) {
    when (route) {
        AppRoute.Registration -> {
            val viewModel: RegistrationViewModel = koinViewModel()
            RegistrationScreen(registrationViewModel = viewModel)
        }
        AppRoute.Rules -> {
            val viewModel: RulesViewModel = koinViewModel()
            RulesScreen(rules = viewModel.getRules())
        }
        AppRoute.Authors -> {
            val viewModel: AuthorsViewModel = koinViewModel()
            AuthorsScreen(viewModel = viewModel)
        }
        AppRoute.Settings -> {
            val viewModel: SettingsViewModel = koinViewModel()
            SettingsScreen(viewModel = viewModel)
        }
        AppRoute.Game -> {
            val viewModel: GameViewModel = koinViewModel()
            GameScreen(viewModel = viewModel)
        }
    }
}