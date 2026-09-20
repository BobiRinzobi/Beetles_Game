package com.phoenix.beetles.feature.settings.di

import com.phoenix.beetles.feature.rules.data.RulesRepositoryImpl
import com.phoenix.beetles.feature.rules.domain.repository.RulesRepository
import com.phoenix.beetles.feature.rules.domain.usecase.GetRulesUseCase
import com.phoenix.beetles.feature.rules.presentation.presenter.RulesViewModel
import com.phoenix.beetles.feature.settings.data.repository.SettingsRepositoryImpl
import com.phoenix.beetles.feature.settings.domain.repository.SettingsRepository
import com.phoenix.beetles.feature.settings.domain.usecase.GetSettingsUseCase
import com.phoenix.beetles.feature.settings.domain.usecase.UpdateSettingsUseCase
import com.phoenix.beetles.feature.settings.presentation.presenter.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl() }

    factory { GetSettingsUseCase(get()) }
    factory { UpdateSettingsUseCase(get()) }

    viewModel { SettingsViewModel(get(), get()) }

}