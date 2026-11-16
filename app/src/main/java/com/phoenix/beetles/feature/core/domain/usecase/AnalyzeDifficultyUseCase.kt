package com.phoenix.beetles.feature.core.domain.usecase

import kotlin.math.abs

class AnalyzeDifficultyUseCase {

    fun execute(scores: List<Int>): Int {
        if (scores.size < 2) return 0

        val current = scores.first().toDouble()
        val previousAvg = scores.drop(1).take(2).average()

        if (previousAvg == 0.0) {
            return when {
                current > 0.0 -> 2
                current < 0.0 -> -2
                else -> 0
            }
        }

        val change = (current - previousAvg) / abs(previousAvg)

        return when {
            change >= 0.20 -> 2
            change <= -0.20 -> -2
            change >= 0.10 -> 1
            change <= -0.10 -> -1
            else -> 0
        }
    }

}
