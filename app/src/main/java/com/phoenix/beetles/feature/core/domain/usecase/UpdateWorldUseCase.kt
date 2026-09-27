package com.phoenix.beetles.feature.core.domain.usecase

import com.phoenix.beetles.feature.core.domain.config.GameConfig
import com.phoenix.beetles.feature.core.domain.entity.GameWorld

class UpdateWorldUseCase(
    private val world: GameWorld,
    private val config: GameConfig,
) {
    fun execute(dt: Float) {

        world.aliveBugs().forEach { bug ->

            bug.move(dt, world.bounds)


            if (!bug.position.isInside(world.bounds)) {
                world.score.miss(config.missPenalty)

                bug.kill()
                world.removeById(bug.id)
            }
        }
    }
}