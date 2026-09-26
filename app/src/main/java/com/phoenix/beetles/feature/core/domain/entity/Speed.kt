package com.phoenix.beetles.feature.core.domain.entity

import kotlin.math.cos
import kotlin.math.sin

data class Speed(val x: Float, val y: Float){
    fun scaledBy(dt: Float): Speed =
        Speed(x * dt, y * dt)

    fun reversedX(): Speed = Speed(-x, y)

    fun reversedY(): Speed = Speed(x, -y)

    companion object {
        fun fromAngle(angleRad: Float, speed: Float): Speed =
            Speed(cos(angleRad) * speed, sin(angleRad) * speed)
    }
}
