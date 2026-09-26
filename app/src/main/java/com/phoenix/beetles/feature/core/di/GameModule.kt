package com.phoenix.beetles.feature.core.di

import android.content.Context
import com.phoenix.beetles.feature.core.domain.config.GameConfig
import com.phoenix.beetles.feature.core.domain.entity.Bounds
import com.phoenix.beetles.feature.core.domain.entity.BugId
import com.phoenix.beetles.feature.core.domain.entity.GameWorld
import com.phoenix.beetles.feature.core.domain.port.RandomProvider
import com.phoenix.beetles.feature.core.domain.port.ScoreRepository
import com.phoenix.beetles.feature.core.domain.usecase.HandleTapUseCase
import com.phoenix.beetles.feature.core.domain.usecase.RestartGameUseCase
import com.phoenix.beetles.feature.core.domain.usecase.SpawnBugsUseCase
import com.phoenix.beetles.feature.core.domain.usecase.UpdateWorldUseCase
import com.phoenix.beetles.feature.core.infrastructure.AndroidRandomProvider
import com.phoenix.beetles.feature.core.infrastructure.SharedPrefsScoreRepository
import com.phoenix.beetles.feature.core.presentation.presenter.GameViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
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
    factory { HandleTapUseCase(get(), get()) }
    factory { UpdateWorldUseCase(get()) }
    factory { RestartGameUseCase(get(), get()) }
    viewModel {
        GameViewModel(
            world = get(),
            handleTap = get(),
            spawnBugs = get(),
            updateWorld = get(),
            restartGame = get(),
            scoreRepository = get(),
        )
    }
}
