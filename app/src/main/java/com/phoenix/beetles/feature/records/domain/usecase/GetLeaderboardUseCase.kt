package com.phoenix.beetles.feature.records.domain.usecase

import com.phoenix.beetles.core.data.local.entity.UserEntity
import com.phoenix.beetles.feature.records.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow

class GetLeaderboardUseCase(
    private val recordsRepository: RecordsRepository
) {
    operator fun invoke(): Flow<List<UserEntity>> = recordsRepository.getLeaderboard()
}