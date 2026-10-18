package com.phoenix.beetles.main

import android.app.Application
import com.phoenix.beetles.core.di.databaseModule
import com.phoenix.beetles.feature.authors.di.authorsModule
import com.phoenix.beetles.feature.core.di.gameModule
import com.phoenix.beetles.feature.gold.di.goldModule
import com.phoenix.beetles.feature.records.di.recordsModule
import com.phoenix.beetles.feature.registration.di.registrationModule
import com.phoenix.beetles.feature.rules.di.rulesModule
import com.phoenix.beetles.feature.settings.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()

            androidContext(this@MainApplication)

            modules(
                databaseModule,
                registrationModule,
                authorsModule,
                rulesModule,
                settingsModule,
                gameModule,
                goldModule,
                recordsModule,
            )
        }
    }
}