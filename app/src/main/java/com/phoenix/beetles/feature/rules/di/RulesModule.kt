package com.phoenix.beetles.feature.rules.di

import androidx.appcompat.widget.AppCompatDrawableManager.get
import com.phoenix.beetles.feature.rules.data.RulesRepositoryImpl
import com.phoenix.beetles.feature.rules.domain.repository.RulesRepository
import com.phoenix.beetles.feature.rules.domain.usecase.GetRulesUseCase
import com.phoenix.beetles.feature.rules.presentation.presenter.RulesViewModel
import okhttp3.internal.platform.android.AndroidSocketAdapter.Companion.factory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val rulesModule = module {
    single<RulesRepository> { RulesRepositoryImpl(get()) }

    factory { GetRulesUseCase(get()) }

    viewModel { RulesViewModel(get()) }

}