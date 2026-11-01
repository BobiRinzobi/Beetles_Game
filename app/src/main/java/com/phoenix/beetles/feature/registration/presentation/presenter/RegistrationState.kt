package com.phoenix.beetles.feature.registration.presentation.presenter

import com.phoenix.beetles.core.data.local.entity.UserEntity

sealed interface RegistrationState {
    data object Initial : RegistrationState
    data object Loading : RegistrationState
    data object Error : RegistrationState
    data class Content(
        val users: List<UserEntity> = emptyList(),
        val activeUser: UserEntity? = null,
    ) : RegistrationState
}
