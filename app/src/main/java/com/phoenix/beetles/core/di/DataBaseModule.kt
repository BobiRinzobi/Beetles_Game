package com.phoenix.beetles.core.di

import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
            .addMigrations(MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get< AppDataBase>().usersDao() }
}

private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE users ADD COLUMN gameSpeed REAL")
        database.execSQL("ALTER TABLE users ADD COLUMN maxBeetlesCount INTEGER")
        database.execSQL("ALTER TABLE users ADD COLUMN boostSpawn INTEGER")
        database.execSQL("ALTER TABLE users ADD COLUMN roundTime INTEGER")
    }
}
