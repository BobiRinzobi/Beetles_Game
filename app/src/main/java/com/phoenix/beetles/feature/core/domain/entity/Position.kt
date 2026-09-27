package com.phoenix.beetles.feature.core.domain.entity

import kotlin.math.hypot

data class Position(val x: Float, val y: Float){
    fun translated(dx: Float, dy: Float) : Position =
        Position(x + dx, y + dy)

    fun distanceTo(other: Position): Float =
        hypot(x - other.x, y - other.y)

    fun isInside(bounds: Bounds, margin: Float = 150.0f) : Boolean =
        x >= -margin && x <= bounds.width + margin && y >= -margin && y <= bounds.height + margin
}
