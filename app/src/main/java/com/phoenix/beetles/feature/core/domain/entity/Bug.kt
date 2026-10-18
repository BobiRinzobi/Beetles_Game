package com.phoenix.beetles.feature.core.domain.entity

import kotlin.math.atan2
import kotlin.math.hypot

class Bug(val id: BugId,
          val type: BugType,
          val config: BugCfg,
          var position: Position,
          var speed: Speed,
          var isAlive: Boolean = true,
          var angle : Float,
          val points: Int = config.points)
{
    fun move(dt: Float, bounds: Bounds, gravity: TiltForce? = null) {
        if (!isAlive) return

        if (gravity != null) {
            speed = speed.plus(gravity.ax * dt, gravity.ay * dt)
                .clamped(config.speed * 3.2f)
        }

        val delta = speed.scaledBy(dt)
        var nextX = position.x + delta.x
        var nextY = position.y + delta.y

        if (gravity != null) {
            val inset = config.radius * 0.35f
            var nextSpeed = speed
            if (nextX < inset) {
                nextX = inset
                nextSpeed = Speed(-nextSpeed.x * WALL_BOUNCE, nextSpeed.y)
            } else if (nextX > bounds.width - inset) {
                nextX = bounds.width - inset
                nextSpeed = Speed(-nextSpeed.x * WALL_BOUNCE, nextSpeed.y)
            }
            if (nextY < inset) {
                nextY = inset
                nextSpeed = Speed(nextSpeed.x, -nextSpeed.y * WALL_BOUNCE)
            } else if (nextY > bounds.height - inset) {
                nextY = bounds.height - inset
                nextSpeed = Speed(nextSpeed.x, -nextSpeed.y * WALL_BOUNCE)
            }
            speed = nextSpeed
            val mag = hypot(speed.x, speed.y)
            if (mag > 8f) {
                angle = atan2(speed.y, speed.x)
            }
        }

        position = Position(nextX, nextY)
    }

    fun contains(point: Position): Boolean =
        position.distanceTo(point) <= config.radius

    fun kill(){isAlive = false;}

    private companion object {
        const val WALL_BOUNCE = 0.42f
    }
}