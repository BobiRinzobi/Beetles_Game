package com.phoenix.beetles.feature.core.domain.usecase

import com.phoenix.beetles.feature.core.domain.repository.GameRepository

class SaveUserRecordUseCase(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(newScore: Int) {
        val activeUser = gameRepository.getActiveUser() ?: return

        if (newScore > activeUser.record) {
            val updatedUser = activeUser.copy(record = newScore)
            gameRepository.updateUser(updatedUser)
        }
    }
}