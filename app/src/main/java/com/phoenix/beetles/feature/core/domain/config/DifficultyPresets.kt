package com.phoenix.beetles.feature.core.domain.config

import com.phoenix.beetles.feature.settings.domain.entity.Settings

object DifficultyPresets {
    const val MIN_LEVEL = 1
    const val MAX_LEVEL = 4
    const val DEFAULT_LEVEL = 1

    private val presets = listOf(
        Settings(gameSpeed = 0.5f, maxBeetlesCount = 5, boostSpawn = 5, roundTime = 120),
        Settings(gameSpeed = 1.0f, maxBeetlesCount = 10, boostSpawn = 10, roundTime = 90),
        Settings(gameSpeed = 1.5f, maxBeetlesCount = 15, boostSpawn = 15, roundTime = 60),
        Settings(gameSpeed = 2.0f, maxBeetlesCount = 20, boostSpawn = 30, roundTime = 5),
        Settings(gameSpeed = 12.0f, maxBeetlesCount = 20, boostSpawn = 30, roundTime = 2)
    )

    fun normalize(level: Int): Int = level.coerceIn(MIN_LEVEL, MAX_LEVEL)

    fun settingsFor(level: Int): Settings = presets[normalize(level) - MIN_LEVEL]

    fun levelFor(settings: Settings): Int? = presets.indexOfFirst { preset ->
        preset.gameSpeed == settings.gameSpeed &&
            preset.maxBeetlesCount == settings.maxBeetlesCount &&
            preset.boostSpawn == settings.boostSpawn &&
            preset.roundTime == settings.roundTime
    }.takeIf { it >= 0 }?.plus(MIN_LEVEL)

}
