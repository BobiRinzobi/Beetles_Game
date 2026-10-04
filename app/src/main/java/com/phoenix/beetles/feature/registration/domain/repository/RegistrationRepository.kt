package com.phoenix.beetles.feature.registration.domain.repository

import com.phoenix.beetles.core.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface RegistrationRepository {
    fun getAllFio(): Flow<List<UserEntity>>
    suspend fun insertUser(user: UserEntity)
    suspend fun setActive(user: UserEntity)
}