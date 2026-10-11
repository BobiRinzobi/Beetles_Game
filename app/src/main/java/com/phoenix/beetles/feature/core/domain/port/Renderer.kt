package com.phoenix.beetles.feature.core.domain.port

import com.phoenix.beetles.feature.core.domain.entity.Bonus
import com.phoenix.beetles.feature.core.domain.entity.Bug

interface Renderer {
    fun clear()
    fun drawBug(bug: Bug)
    fun drawBonus(bonus: Bonus)
    fun drawScore(score: Int, misses: Int)
    fun drawGameOver(finalScore: Int, best: Int)
}