package com.phoenix.beetles.feature.core.domain.usecase

import com.phoenix.beetles.core.data.local.entity.UserEntity
import com.phoenix.beetles.feature.core.domain.repository.GameRepository

class UpdateUserRecordUseCase(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(user: UserEntity) {
        gameRepository.updateUser(user)
    }
}