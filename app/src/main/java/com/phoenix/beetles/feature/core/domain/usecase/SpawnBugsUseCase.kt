package com.phoenix.beetles.feature.core.domain.usecase

import com.phoenix.beetles.feature.core.domain.entity.GameWorld
import com.phoenix.beetles.feature.core.domain.config.GameConfig
import com.phoenix.beetles.feature.core.domain.entity.Bug
import com.phoenix.beetles.feature.core.domain.entity.BugCfg
import com.phoenix.beetles.feature.core.domain.entity.BugId
import com.phoenix.beetles.feature.core.domain.entity.BugType
import com.phoenix.beetles.feature.core.domain.entity.Position
import com.phoenix.beetles.feature.core.domain.entity.Speed
import com.phoenix.beetles.feature.core.domain.port.RandomProvider
import kotlin.math.PI

class SpawnBugsUseCase(
    private val world: GameWorld,
    private val config: GameConfig,
    private val random: RandomProvider,
    private val idGenerator: () -> BugId,
) {
    private var accumulator = 0f

    fun tick(dt: Float) {
        if (world.bounds.width <= 1f || world.bounds.height <= 1f) return
        accumulator += dt
        while (accumulator >= config.spawnIntervalSec) {
            accumulator -= config.spawnIntervalSec
            if (world.aliveBugs().size < config.maxBugs) {
                world.add(createBug())
            }
        }
    }

    fun reset() {
        accumulator = 0f
    }

    private fun createBug(): Bug {
        val bugConfig = pickConfig()
        val angle = random.nextFloat(0f, (2 * PI).toFloat())
        val speed = Speed.fromAngle(angle, bugConfig.speed)

        val margin = bugConfig.radius
        val position = Position(
            x = random.nextFloat(margin, world.bounds.width - margin),
            y = random.nextFloat(margin, world.bounds.height - margin),
        )
        val randomType = BugType.values().random()

        return Bug(
            id = idGenerator(),
            type = randomType,
            config = bugConfig,
            position = position,
            speed = speed,
            angle = angle
        )
    }

    private fun pickConfig(): BugCfg {
        val configs = config.bugConfig.values.toList()
        val roll = random.nextFloat()
        for (cfg in configs) {
            if (roll <= 0f) return cfg
        }
        return configs.last()
    }
}