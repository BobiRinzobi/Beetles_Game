package com.phoenix.beetles.feature.core.domain.usecase

import com.phoenix.beetles.feature.core.domain.entity.GameWorld

class UpdateWorldUseCase(private val world: GameWorld) {
    fun execute(dt: Float){
        world.aliveBugs().forEach {it.move(dt, world.bounds)}
    }
}