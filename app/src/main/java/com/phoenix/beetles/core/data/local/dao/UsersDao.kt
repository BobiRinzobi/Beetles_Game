package com.phoenix.beetles.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.phoenix.beetles.core.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UsersDao {


    @Query("SELECT * FROM users ORDER BY fio DESC")
    fun getAllUsersFio(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users ORDER BY record DESC")
    fun getAllUsersRecord(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE users.id = :id ")
    fun getUser(id : String) : Flow<UserEntity?>

    @Update
    suspend fun updateUser(user : UserEntity)

    @Delete
    suspend fun deleteUser(user : UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user : UserEntity)

    @Query("SELECT * FROM users WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveUser(): UserEntity?

    @Query("UPDATE users SET isActive = 0")
    suspend fun clearActiveUser()
}