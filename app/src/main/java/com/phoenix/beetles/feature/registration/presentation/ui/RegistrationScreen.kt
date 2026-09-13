package com.phoenix.beetles.feature.registration.presentation.ui

import android.widget.ProgressBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.phoenix.beetles.feature.registration.presentation.presenter.RegistrationState
import com.phoenix.beetles.feature.registration.presentation.presenter.RegistrationViewModel

@Composable
fun registrationScreen(
    registrationViewModel: RegistrationViewModel
) {
    val state by registrationViewModel.state.observeAsState(RegistrationState.Loading)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when(val currentState = state){
            is RegistrationState.Initial,
            is RegistrationState.Loading -> println("загрузка")

            is RegistrationState.Error -> registrationError()
            is RegistrationState.Content -> registrationContent()
        }
    }

}