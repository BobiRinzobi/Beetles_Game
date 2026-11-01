package com.phoenix.beetles.feature.core.domain.usecase

import kotlin.math.abs

class AnalyzeDifficultyUseCase {

    fun execute(scores: List<Int>): Int {
        if (scores.size < MIN_RESULTS) return 0

        val current = scores.first().toDouble()
        val previousAvg = scores.drop(1).take(PREVIOUS_RESULTS).average()

        if (previousAvg == 0.0) {
            return when {
                current > 0.0 -> 2
                current < 0.0 -> -2
                else -> 1
            }
        }

        val change = (current - previousAvg) / abs(previousAvg)

        return when {
            change >= CHANGE_STEP -> 2
            change <= -CHANGE_STEP -> -2
            else -> 0
        }
    }

    private companion object {
        const val MIN_RESULTS = 3
        const val PREVIOUS_RESULTS = 2
        const val CHANGE_STEP = 0.10
    }
}
