package com.phoenix.beetles.feature.registration.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.phoenix.beetles.feature.registration.presentation.presenter.RegistrationState
import com.phoenix.beetles.feature.registration.presentation.presenter.RegistrationViewModel

@Composable
fun RegistrationScreen(
    registrationViewModel: RegistrationViewModel
) {
    LaunchedEffect(Unit) {
        registrationViewModel.loadData()
    }

    val state by registrationViewModel.state.observeAsState(RegistrationState.Initial)

    Box(modifier = Modifier.fillMaxSize()) {
        when (val currentState = state) {
            is RegistrationState.Initial,
            is RegistrationState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is RegistrationState.Error -> {
                RegistrationError()
            }

            is RegistrationState.Content -> {
                RegistrationContent(
                    users = currentState.users,
                    activeUser = currentState.activeUser,
                    onRegisterUser = { user ->
                        registrationViewModel.registerUser(user)
                    },
                    onSelectUser = { user ->
                        registrationViewModel.setActiveUser(user)
                    }
                )
            }
        }
    }
}
