package com.phoenix.beetles.core.di

import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.phoenix.beetles.core.data.local.AppDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import kotlin.jvm.java

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDataBase::class.java,
            "beetles_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get< AppDataBase>().usersDao() }
}