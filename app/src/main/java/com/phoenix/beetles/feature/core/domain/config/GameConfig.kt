package com.phoenix.beetles.feature.core.domain.config

import com.phoenix.beetles.feature.core.domain.entity.BugCfg
import com.phoenix.beetles.feature.core.domain.entity.BugType

data class GameConfig(
    val bugConfig: Map<BugType, BugCfg>,
    val spawnIntervalSec: Float,
    val missPenalty: Int,
    val maxBugs: Int,
    val roundTime: Int,
) {
    companion object {
        val DEFAULT = GameConfig(
            bugConfig = mapOf(
                BugType.FLY to BugCfg(
                    type = BugType.FLY,
                    radius = 180f,
                    speed = 210f,
                    points = 10,
                    spriteKey = "fly",
                ),
                BugType.BEE to BugCfg(
                    type = BugType.BEE,
                    radius = 180f,
                    speed = 260f,
                    points = 25,
                    spriteKey = "bee",
                ),
                BugType.BEETLE to BugCfg(
                    type = BugType.BEETLE,
                    radius = 180f,
                    speed = 200f,
                    points = 5,
                    spriteKey = "beetle",
                ),
            ),
            spawnIntervalSec = 0.8f,
            missPenalty = 5,
            maxBugs = 12,
            roundTime = 60,
        )
    }
}