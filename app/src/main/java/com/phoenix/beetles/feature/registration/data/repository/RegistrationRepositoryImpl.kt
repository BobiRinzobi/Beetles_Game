package com.phoenix.beetles.feature.registration.data.repository

import com.phoenix.beetles.core.data.local.dao.UsersDao
import com.phoenix.beetles.core.data.local.entity.UserEntity
import com.phoenix.beetles.feature.registration.domain.repository.RegistrationRepository
import kotlinx.coroutines.flow.Flow

class RegistrationRepositoryImpl(
    private val dataSource: UsersDao
) : RegistrationRepository {

    override fun getAllFio(): Flow<List<UserEntity>> = dataSource.getAllUsersFio()

    override suspend fun insertUser(user: UserEntity) {
        dataSource.insertUser(user)
    }

    override suspend fun setActive(user: UserEntity) {
        dataSource.clearActiveUser()
        dataSource.insertUser(user.copy(isActive = true))
    }
}