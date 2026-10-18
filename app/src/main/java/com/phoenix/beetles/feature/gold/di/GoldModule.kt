package com.phoenix.beetles.feature.gold.di

import com.phoenix.beetles.feature.gold.data.GoldRepositoryImpl
import com.phoenix.beetles.feature.gold.domain.repository.GoldRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val goldModule = module {
    single<GoldRepository> { GoldRepositoryImpl(androidContext()) }
}
