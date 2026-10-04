package com.phoenix.beetles.feature.registration.domain.usecase

import com.phoenix.beetles.core.data.local.entity.UserEntity
import com.phoenix.beetles.feature.registration.domain.repository.RegistrationRepository
import kotlinx.coroutines.flow.Flow

class GetAllUsersFioUseCase(
    private val repository: RegistrationRepository
) {
    operator fun invoke(): Flow<List<UserEntity>> = repository.getAllFio()
}