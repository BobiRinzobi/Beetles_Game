package com.phoenix.beetles.feature.core.domain.entity

data class Bonus(
    val position: Position,
    val radius: Float,
) {
    fun contains(point: Position): Boolean =
        position.distanceTo(point) <= radius
}
