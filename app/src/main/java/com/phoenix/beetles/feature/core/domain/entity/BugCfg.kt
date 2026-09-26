package com.phoenix.beetles.feature.core.domain.entity

data class BugCfg(
    val type: BugType,
    val speed: Float,
    val radius: Float,
    val points: Int,
    val spriteKey: String
)
