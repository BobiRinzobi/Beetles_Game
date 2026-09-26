package com.phoenix.beetles.feature.core.domain.usecase

import com.phoenix.beetles.feature.core.domain.entity.GameWorld
import com.phoenix.beetles.feature.core.domain.config.GameConfig
import com.phoenix.beetles.feature.core.domain.entity.Position

sealed interface TapResult {
    data class Hit(val points: Int) : TapResult
    object Miss : TapResult
}

class HandleTapUseCase (
    private val world: GameWorld,
    private val config: GameConfig,
) {
    fun execute(point: Position): TapResult {
        val bug = world.findBugAt(point)
        return if (bug != null) {
            bug.kill()
            world.removeById(bug.id)
            world.score.add(bug.config.points)
            TapResult.Hit(bug.config.points)
        } else {
            world.score.miss(config.missPenalty)
            TapResult.Miss
        }
    }
}