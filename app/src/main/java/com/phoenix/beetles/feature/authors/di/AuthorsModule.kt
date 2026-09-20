package com.phoenix.beetles.feature.authors.di

import com.phoenix.beetles.feature.authors.data.AuthorsRepositoryImpl
import com.phoenix.beetles.feature.authors.domain.repository.AuthorsRepository
import com.phoenix.beetles.feature.authors.domain.usecase.GetAuthorsUseCase
import com.phoenix.beetles.feature.authors.presentation.presenter.AuthorsViewModel
import com.phoenix.beetles.feature.rules.data.RulesRepositoryImpl
import com.phoenix.beetles.feature.rules.domain.repository.RulesRepository
import com.phoenix.beetles.feature.rules.domain.usecase.GetRulesUseCase
import com.phoenix.beetles.feature.rules.presentation.presenter.RulesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authorsModule = module {
    single<AuthorsRepository> { AuthorsRepositoryImpl() }

    factory { GetAuthorsUseCase(get()) }

    viewModel { AuthorsViewModel(get()) }

}