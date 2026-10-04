package com.phoenix.beetles.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.phoenix.beetles.feature.authors.presentation.presenter.AuthorsViewModel
import com.phoenix.beetles.feature.authors.presentation.ui.AuthorsScreen
import com.phoenix.beetles.feature.core.presentation.presenter.GameViewModel
import com.phoenix.beetles.feature.core.presentation.ui.GameScreen
import com.phoenix.beetles.feature.records.presentation.presenter.RecordsViewModel
import com.phoenix.beetles.feature.records.presentation.ui.RecordsScreen
import com.phoenix.beetles.feature.registration.presentation.presenter.RegistrationViewModel
import com.phoenix.beetles.feature.registration.presentation.ui.RegistrationScreen
import com.phoenix.beetles.feature.rules.presentation.presenter.RulesViewModel
import com.phoenix.beetles.feature.rules.presentation.ui.RulesScreen
import com.phoenix.beetles.feature.settings.presentation.presenter.SettingsViewModel
import com.phoenix.beetles.feature.settings.presentation.ui.SettingsScreen
import com.phoenix.beetles.main.MenuScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationPanel(
    route: AppRoute,
    onNavigateTo: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    when (route) {
        AppRoute.Registration -> {
            val viewModel: RegistrationViewModel = koinViewModel()
            RegistrationScreen(registrationViewModel = viewModel)
        }
        AppRoute.Game -> {
            val viewModel: GameViewModel = koinViewModel()
            GameScreen(viewModel = viewModel)
        }
        AppRoute.Menu -> {
            MenuScreen(onNavigateTo = onNavigateTo, modifier = modifier)
        }
        AppRoute.Records -> {
            val viewModel: RecordsViewModel = koinViewModel()
            RecordsScreen(viewModel = viewModel, modifier = modifier)
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
    }
}