package com.phoenix.beetles.feature.settings.di

import com.phoenix.beetles.feature.settings.data.repository.SettingsRepositoryImpl
import com.phoenix.beetles.feature.settings.domain.repository.SettingsRepository
import com.phoenix.beetles.feature.settings.domain.usecase.GetSettingsUseCase
import com.phoenix.beetles.feature.settings.domain.usecase.UpdateSettingsUseCase
import com.phoenix.beetles.feature.settings.presentation.presenter.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl(androidContext()) }

    factory { GetSettingsUseCase(get()) }
    factory { UpdateSettingsUseCase(get()) }

    viewModel { SettingsViewModel(get(), get(), get(), get()) }

}
