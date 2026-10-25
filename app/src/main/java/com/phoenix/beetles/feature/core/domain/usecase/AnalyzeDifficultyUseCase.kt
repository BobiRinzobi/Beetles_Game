package com.phoenix.beetles.feature.core.domain.usecase

class AnalyzeDifficultyUseCase {

    fun execute(scores: List<Int>): Int {
        if (scores.size < 3) return 0

        val recent = scores.take(2).average()
        val older = scores.takeLast(2).average()

        if (older == 0.0) {
            return if (recent > 0) 1 else 0
        }

        val change = (recent - older) / older

        return when {
            change >= 0.15 -> 1
            change <= -0.15 -> -1
            else -> 0
        }
    }
}