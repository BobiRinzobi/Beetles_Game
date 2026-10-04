package com.phoenix.beetles.feature.core.data.repository

import com.phoenix.beetles.core.data.local.dao.UsersDao
import com.phoenix.beetles.core.data.local.entity.UserEntity
import com.phoenix.beetles.feature.core.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow

class GameRepositoryImpl(
    private val usersDao: UsersDao
) : GameRepository {

    override suspend fun getActiveUser(): UserEntity? = usersDao.getActiveUser()

    override fun getUser(id: String): Flow<UserEntity?> = usersDao.getUser(id)

    override suspend fun updateUser(user: UserEntity) = usersDao.updateUser(user)
}