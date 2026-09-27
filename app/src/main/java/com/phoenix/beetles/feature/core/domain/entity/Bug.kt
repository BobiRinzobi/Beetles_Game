package com.phoenix.beetles.feature.core.domain.entity

class Bug(val id: BugId,
          val type: BugType,
          val config: BugCfg,
          var position: Position,
          var speed: Speed,
          var isAlive: Boolean = true,
          var angle : Float )
{
    fun move(dt: Float, bounds: Bounds){
        if(!isAlive) return;

        val delta = speed.scaledBy(dt)
        position = position.translated(delta.x, delta.y)
    }

    fun contains(point: Position): Boolean =
        position.distanceTo(point) <= config.radius

    fun kill(){isAlive = false;}

}