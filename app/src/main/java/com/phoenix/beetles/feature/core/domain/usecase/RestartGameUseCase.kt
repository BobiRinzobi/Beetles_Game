package com.phoenix.beetles.feature.core.domain.usecase

import com.phoenix.beetles.feature.core.domain.entity.GameWorld

class RestartGameUseCase(
    private val world: GameWorld,
    private val spawn: SpawnBugsUseCase
) {
    fun execute(){
        world.clear()
        spawn.reset()
    }
}