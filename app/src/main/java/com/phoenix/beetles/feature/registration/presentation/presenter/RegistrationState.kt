package com.phoenix.beetles.feature.registration.presentation.presenter

import com.phoenix.beetles.feature.registration.domain.entity.User

sealed interface RegistrationState {

    data object Initial : RegistrationState

    data object Loading : RegistrationState

    data object  Error : RegistrationState

    data object Content : RegistrationState

}