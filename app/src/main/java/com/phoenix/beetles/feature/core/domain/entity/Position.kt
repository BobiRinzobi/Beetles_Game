package com.phoenix.beetles.feature.core.domain.entity

import kotlin.math.hypot

data class Position(val x: Float, val y: Float){
    fun translated(dx: Float, dy: Float) : Position =
        Position(x + dx, y + dy)

    fun distanceTo(other: Position): Float =
        hypot(x - other.x, y - other.y)

    fun isInside(bounds: Bounds) : Boolean =
        x >= 0f && x <= bounds.width && y >= 0f && y <= bounds.height
}
