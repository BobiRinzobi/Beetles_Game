package com.phoenix.beetles.feature.records.di

import com.phoenix.beetles.feature.records.data.RecordsRepositoryImpl
import com.phoenix.beetles.feature.records.domain.repository.RecordsRepository
import com.phoenix.beetles.feature.records.domain.usecase.GetLeaderboardUseCase
import com.phoenix.beetles.feature.records.presentation.presenter.RecordsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val recordsModule = module {
    single<RecordsRepository> { RecordsRepositoryImpl(get()) }
    factoryOf(::GetLeaderboardUseCase)
    viewModelOf(::RecordsViewModel)
}