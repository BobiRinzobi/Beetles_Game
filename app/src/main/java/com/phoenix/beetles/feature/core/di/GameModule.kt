package com.phoenix.beetles.feature.core.di

import android.content.Context
import com.phoenix.beetles.feature.core.data.repository.GameRepositoryImpl
import com.phoenix.beetles.feature.core.domain.config.GameConfig
import com.phoenix.beetles.feature.core.domain.entity.Bounds
import com.phoenix.beetles.feature.core.domain.entity.BugId
import com.phoenix.beetles.feature.core.domain.entity.GameWorld
import com.phoenix.beetles.feature.core.domain.port.RandomProvider
import com.phoenix.beetles.feature.core.domain.port.ScoreRepository
import com.phoenix.beetles.feature.core.domain.port.SfxPlayer
import com.phoenix.beetles.feature.core.domain.repository.GameRepository
import com.phoenix.beetles.feature.core.domain.usecase.AnalyzeDifficultyUseCase
import com.phoenix.beetles.feature.core.domain.usecase.HandleTapUseCase
import com.phoenix.beetles.feature.core.domain.usecase.RestartGameUseCase
import com.phoenix.beetles.feature.core.domain.usecase.SaveUserRecordUseCase
import com.phoenix.beetles.feature.core.domain.usecase.SpawnBonusUseCase
import com.phoenix.beetles.feature.core.domain.usecase.SpawnBugsUseCase
import com.phoenix.beetles.feature.core.domain.usecase.UpdateWorldUseCase
import com.phoenix.beetles.feature.core.infrastructure.AndroidRandomProvider
import com.phoenix.beetles.feature.core.infrastructure.AndroidSfxPlayer
import com.phoenix.beetles.feature.core.infrastructure.SharedPrefsScoreRepository
import com.phoenix.beetles.feature.gold.domain.usecase.SpawnGoldenBugUseCase
import com.phoenix.beetles.feature.core.presentation.presenter.GameViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import java.util.UUID

val gameModule = module {
    single { GameConfig.DEFAULT }
    single<RandomProvider> { AndroidRandomProvider() }
    single<ScoreRepository> {
        SharedPrefsScoreRepository(
            androidContext().getSharedPreferences("bugs", Context.MODE_PRIVATE)
        )
    }
    single { GameWorld(Bounds(width = 1f, height = 1f)) }
    single {
        SpawnBugsUseCase(
            world = get(),
            config = get(),
            random = get(),
            idGenerator = { BugId(UUID.randomUUID()) },
        )
    }
    single<GameRepository> { GameRepositoryImpl(get()) }
    single<SfxPlayer> { AndroidSfxPlayer(androidContext()) }
    single {
        SpawnBonusUseCase(
            world = get(),
            config = get(),
            random = get(),
        )
    }
    factory {
        SpawnGoldenBugUseCase(
            world = get(),
            config = get(),
            random = get(),
            idGenerator = { BugId(UUID.randomUUID()) },
        )
    }
    factory { HandleTapUseCase(get(), get()) }
    factory { UpdateWorldUseCase(get(), get()) }
    factory { RestartGameUseCase(get(), get(), get()) }
    factory { AnalyzeDifficultyUseCase() }
    factoryOf(::SaveUserRecordUseCase)
    viewModelOf(::GameViewModel)
}