package com.phoenix.beetles.feature.records.data

import com.phoenix.beetles.core.data.local.dao.UsersDao
import com.phoenix.beetles.core.data.local.entity.UserEntity
import com.phoenix.beetles.feature.records.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow

class RecordsRepositoryImpl(
    private val usersDao: UsersDao
) : RecordsRepository {

    override fun getLeaderboard(): Flow<List<UserEntity>> = usersDao.getAllUsersRecord()
}