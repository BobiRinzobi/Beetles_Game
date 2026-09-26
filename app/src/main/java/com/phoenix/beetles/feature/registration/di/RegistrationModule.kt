package com.phoenix.beetles.feature.registration.di

import com.phoenix.beetles.feature.registration.presentation.presenter.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val registrationModule = module {
    viewModel { RegistrationViewModel() }
}
