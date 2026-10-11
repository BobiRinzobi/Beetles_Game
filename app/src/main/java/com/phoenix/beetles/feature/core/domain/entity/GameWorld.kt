package com.phoenix.beetles.feature.core.domain.entity


class GameWorld(var bounds: Bounds) {

    private val _bugs = mutableListOf<Bug>()
    val bugs: List<Bug> get() = _bugs

    val score = Score()

    var bonus: Bonus? = null
    var gravityTimeLeft: Float = 0f
    val isGravityActive: Boolean get() = gravityTimeLeft > 0f

    fun add(bug: Bug) {
        _bugs.add(bug)
    }

    fun removeById(id: BugId) {
        _bugs.removeIf { it.id == id }
    }

    fun findBugAt(point: Position): Bug? =
        _bugs.asReversed().firstOrNull { it.isAlive && it.contains(point) }

    fun findBonusAt(point: Position): Bonus? =
        bonus?.takeIf { it.contains(point) }

    fun collectBonus(durationSec: Float) {
        bonus = null
        gravityTimeLeft = durationSec
    }

    fun tickGravity(dt: Float) {
        if (gravityTimeLeft <= 0f) {
            gravityTimeLeft = 0f
            return
        }
        gravityTimeLeft = (gravityTimeLeft - dt).coerceAtLeast(0f)
    }

    fun aliveBugs(): List<Bug> = _bugs.filter { it.isAlive }

    fun clear() {
        _bugs.clear()
        bonus = null
        gravityTimeLeft = 0f
        score.reset()
    }
}