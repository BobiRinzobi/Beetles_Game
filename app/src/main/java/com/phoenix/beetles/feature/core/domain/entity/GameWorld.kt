package com.phoenix.beetles.feature.core.domain.entity


class GameWorld(var bounds: Bounds) {

    private val _bugs = mutableListOf<Bug>()
    val bugs: List<Bug> get() = _bugs

    val score = Score()

    fun add(bug: Bug) {
        _bugs.add(bug)
    }

    fun removeById(id: BugId) {
        _bugs.removeIf { it.id == id }
    }

    fun findBugAt(point: Position): Bug? =
        _bugs.asReversed().firstOrNull { it.isAlive && it.contains(point) }

    fun aliveBugs(): List<Bug> = _bugs.filter { it.isAlive }

    fun clear() {
        _bugs.clear()
        score.reset()
    }
}