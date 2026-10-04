package com.phoenix.beetles.feature.registration.domain.usecase

import com.phoenix.beetles.core.data.local.entity.UserEntity
import com.phoenix.beetles.feature.registration.domain.repository.RegistrationRepository

class SetActiveUserUseCase(
    private val repository: RegistrationRepository
) {
    suspend operator fun invoke(user: UserEntity) = repository.setActive(user)
}