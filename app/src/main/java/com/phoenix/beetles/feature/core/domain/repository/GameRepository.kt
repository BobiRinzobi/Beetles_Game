package com.phoenix.beetles.feature.core.domain.repository

import com.phoenix.beetles.core.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun getActiveUser(): UserEntity?
    suspend fun updateUser(user: UserEntity)
    fun getUser(id: String): Flow<UserEntity?>
}