package com.phoenix.beetles.feature.records.domain.repository

import com.phoenix.beetles.core.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface RecordsRepository {
    fun getLeaderboard(): Flow<List<UserEntity>>
}