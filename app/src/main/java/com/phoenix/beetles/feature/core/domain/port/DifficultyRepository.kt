package com.phoenix.beetles.feature.core.domain.port

import kotlinx.coroutines.flow.StateFlow

interface DifficultyRepository {
    fun getLevel(): Int
    fun observeLevel(): StateFlow<Int>
    fun saveLevel(level: Int)

}

