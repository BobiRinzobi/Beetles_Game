package com.phoenix.beetles.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.phoenix.beetles.core.data.local.dao.UsersDao
import com.phoenix.beetles.core.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 2,
    exportSchema = false
)

abstract class AppDataBase : RoomDatabase() {

    abstract fun usersDao(): UsersDao
}