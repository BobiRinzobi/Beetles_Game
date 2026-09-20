package com.phoenix.beetles.feature.registration.di

import com.phoenix.beetles.feature.registration.presentation.presenter.RegistrationViewModel
import com.phoenix.beetles.feature.rules.data.RulesRepositoryImpl
import com.phoenix.beetles.feature.rules.domain.repository.RulesRepository
import com.phoenix.beetles.feature.rules.domain.usecase.GetRulesUseCase
import com.phoenix.beetles.feature.rules.presentation.presenter.RulesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val registrationModule = module {

    viewModel { RegistrationViewModel() }

}