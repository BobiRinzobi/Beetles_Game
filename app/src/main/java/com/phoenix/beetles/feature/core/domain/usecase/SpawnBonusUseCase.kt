package com.phoenix.beetles.feature.core.domain.usecase

import com.phoenix.beetles.feature.core.domain.config.GameConfig
import com.phoenix.beetles.feature.core.domain.entity.Bonus
import com.phoenix.beetles.feature.core.domain.entity.GameWorld
import com.phoenix.beetles.feature.core.domain.entity.Position
import com.phoenix.beetles.feature.core.domain.port.RandomProvider

class SpawnBonusUseCase(
    private val world: GameWorld,
    config: GameConfig,
    private val random: RandomProvider,
) {
    private var config: GameConfig = config
    private var accumulator = 0f

    fun updateConfig(config: GameConfig) {
        this.config = config
    }

    fun tick(dt: Float) {
        if (world.bounds.width <= 1f || world.bounds.height <= 1f) return
        accumulator += dt
        while (accumulator >= config.bonusIntervalSec) {
            accumulator -= config.bonusIntervalSec
            world.bonus = createBonus()
        }
    }

    fun reset() {
        accumulator = 0f
        world.bonus = null
    }

    private fun createBonus(): Bonus {
        val radius = config.bonusRadius
        val topMargin = radius + 140f
        val side = radius + 24f
        val maxX = (world.bounds.width - side).coerceAtLeast(side + 1f)
        val maxY = (world.bounds.height - side).coerceAtLeast(topMargin + 1f)
        return Bonus(
            position = Position(
                x = random.nextFloat(side, maxX),
                y = random.nextFloat(topMargin, maxY),
            ),
            radius = radius,
        )
    }
}
