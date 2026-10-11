package com.phoenix.beetles.feature.core.domain.usecase

import com.phoenix.beetles.feature.core.domain.config.GameConfig
import com.phoenix.beetles.feature.core.domain.entity.GameWorld
import com.phoenix.beetles.feature.core.domain.entity.TiltForce

class UpdateWorldUseCase(
    private val world: GameWorld,
    private val config: GameConfig,
) {
    fun execute(dt: Float, tilt: TiltForce = TiltForce()) {
        val gravity = if (world.isGravityActive) {
            TiltForce(
                ax = tilt.ax * config.gravityPixelsPerG / STANDARD_G,
                ay = tilt.ay * config.gravityPixelsPerG / STANDARD_G,
            )
        } else {
            null
        }

        world.aliveBugs().forEach { bug ->
            bug.move(dt, world.bounds, gravity)

            if (!world.isGravityActive && !bug.position.isInside(world.bounds)) {
                world.score.miss(config.missPenalty)

                bug.kill()
                world.removeById(bug.id)
            }
        }
    }

    private companion object {
        const val STANDARD_G = 9.81f
    }
}