package com.phoenix.beetles.feature.registration.di

import com.phoenix.beetles.feature.registration.data.repository.RegistrationRepositoryImpl
import com.phoenix.beetles.feature.registration.domain.repository.RegistrationRepository
import com.phoenix.beetles.feature.registration.domain.usecase.GetAllUsersFioUseCase
import com.phoenix.beetles.feature.registration.domain.usecase.InsertUserUseCase
import com.phoenix.beetles.feature.registration.domain.usecase.SetActiveUserUseCase
import com.phoenix.beetles.feature.registration.presentation.presenter.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val registrationModule = module {
    single<RegistrationRepository> { RegistrationRepositoryImpl(get()) }
    factoryOf(::GetAllUsersFioUseCase)
    factoryOf(::InsertUserUseCase)
    factoryOf(::SetActiveUserUseCase)
    viewModel { RegistrationViewModel(get(), get(), get(), get(), get()) }
}
