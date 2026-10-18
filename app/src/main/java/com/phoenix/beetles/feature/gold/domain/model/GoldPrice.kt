package com.phoenix.beetles.feature.gold.domain.model

import kotlin.math.roundToInt

data class GoldPrice(
    val pricePerGram: Double,
    val date: String,
) {
    val bonusPoints: Int
        get() = (pricePerGram / RUBLES_PER_POINT).roundToInt().coerceAtLeast(1)

    companion object {
        const val RUBLES_PER_POINT = 250.0
        const val FALLBACK_BONUS_POINTS = 1
    }
}
