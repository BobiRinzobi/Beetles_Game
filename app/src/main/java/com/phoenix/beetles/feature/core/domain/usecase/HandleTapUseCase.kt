package com.phoenix.beetles.feature.core.domain.usecase

import com.phoenix.beetles.feature.core.domain.entity.GameWorld
import com.phoenix.beetles.feature.core.domain.config.GameConfig
import com.phoenix.beetles.feature.core.domain.entity.Position

sealed interface TapResult {
    data class Hit(val points: Int) : TapResult
    object Bonus : TapResult
    object Miss : TapResult
}

class HandleTapUseCase (
    private val world: GameWorld,
    private val config: GameConfig,
) {
    fun execute(point: Position): TapResult {
        if (world.findBonusAt(point) != null) {
            world.collectBonus(config.gravityModeDurationSec)
            return TapResult.Bonus
        }
        val bug = world.findBugAt(point)
        return if (bug != null) {
            bug.kill()
            world.removeById(bug.id)
            world.score.add(bug.points)
            TapResult.Hit(bug.points)
        } else {
            world.score.miss(config.missPenalty)
            TapResult.Miss
        }
    }
}