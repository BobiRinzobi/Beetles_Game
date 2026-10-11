package com.phoenix.beetles.feature.core.domain.entity

import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

data class Speed(val x: Float, val y: Float){
    fun scaledBy(dt: Float): Speed =
        Speed(x * dt, y * dt)

    fun reversedX(): Speed = Speed(-x, y)

    fun reversedY(): Speed = Speed(x, -y)

    fun plus(dx: Float, dy: Float): Speed = Speed(x + dx, y + dy)

    fun clamped(max: Float): Speed {
        val mag = hypot(x.toDouble(), y.toDouble()).toFloat()
        if (mag <= max || mag == 0f) return this
        val scale = max / mag
        return Speed(x * scale, y * scale)
    }

    companion object {
        fun fromAngle(angleRad: Float, speed: Float): Speed =
            Speed(cos(angleRad) * speed, sin(angleRad) * speed)
    }
}
