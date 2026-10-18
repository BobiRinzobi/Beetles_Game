package com.phoenix.beetles.feature.gold.domain.usecase

import com.phoenix.beetles.feature.core.domain.config.GameConfig
import com.phoenix.beetles.feature.core.domain.entity.Bug
import com.phoenix.beetles.feature.core.domain.entity.BugId
import com.phoenix.beetles.feature.core.domain.entity.BugType
import com.phoenix.beetles.feature.core.domain.entity.GameWorld
import com.phoenix.beetles.feature.core.domain.entity.Position
import com.phoenix.beetles.feature.core.domain.entity.Speed
import com.phoenix.beetles.feature.core.domain.port.RandomProvider
import kotlin.math.PI

class SpawnGoldenBugUseCase(
    private val world: GameWorld,
    private val config: GameConfig,
    private val random: RandomProvider,
    private val idGenerator: () -> BugId,
) {
    fun execute(rewardPoints: Int): Boolean {
        val bounds = world.bounds
        if (bounds.width <= 1f || bounds.height <= 1f) return false
        val bugConfig = config.bugConfig[BugType.GOLD_BEETLE] ?: return false
        val angle = random.nextFloat(0f, (2 * PI).toFloat())
        val speed = Speed.fromAngle(angle, bugConfig.speed)
        val x = randomCoordinate(bounds.width, bugConfig.radius)
        val y = randomCoordinate(bounds.height, bugConfig.radius)

        world.add(
            Bug(
                id = idGenerator(),
                type = BugType.GOLD_BEETLE,
                config = bugConfig,
                position = Position(x, y),
                speed = speed,
                angle = angle,
                points = rewardPoints.coerceAtLeast(1),
            )
        )
        return true
    }

    private fun randomCoordinate(length: Float, radius: Float): Float {
        val from = radius.coerceAtMost(length / 2f)
        val until = (length - radius).coerceAtLeast(length / 2f)
        return if (until <= from) length / 2f else random.nextFloat(from, until)
    }
}
