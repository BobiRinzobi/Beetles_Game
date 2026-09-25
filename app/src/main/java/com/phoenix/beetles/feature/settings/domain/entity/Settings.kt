package com.phoenix.beetles.feature.settings.domain.entity

data class Settings(
    val gameSpeed: Float = 1.0f,
    val maxBeetlesCount: Int = 10,
    val boostSpawn: Int = 15,
    val roundTime: Int = 60,

    val gameSpeedOptions: List<Float> = listOf(0.5f, 1.0f, 1.5f, 2.0f),
    val maxBeetlesCountOptions: List<Int> = listOf(5, 10, 15, 20, 30),
    val boostSpawnOptions: List<Int> = listOf(5, 10, 15, 30),
    val roundTimeOptions: List<Int> = listOf(30, 60, 90, 120)
)
