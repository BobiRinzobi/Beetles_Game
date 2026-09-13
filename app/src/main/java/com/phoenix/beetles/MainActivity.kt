package com.phoenix.beetles

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.phoenix.beetles.feature.registration.presentation.presenter.RegistrationViewModel
import com.phoenix.beetles.feature.registration.presentation.ui.RegistrationScreen
import com.phoenix.beetles.ui.theme.Beetles_GameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Beetles_GameTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) {
                    val viewModel: RegistrationViewModel = viewModel()
                    RegistrationScreen(registrationViewModel = viewModel)
                }
            }
        }
    }
}
