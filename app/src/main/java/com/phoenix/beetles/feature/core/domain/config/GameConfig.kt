package com.phoenix.beetles.feature.core.domain.config

import com.phoenix.beetles.feature.core.domain.entity.BugCfg
import com.phoenix.beetles.feature.core.domain.entity.BugType

data class GameConfig(
    val bugConfig: Map <BugType, BugCfg>,
    val spawnIntervalSec: Float,
    val missPenalty: Int,
    val maxBugs: Int,
)  {
    companion object {
        val DEFAULT = GameConfig(
            bugConfig = mapOf(
                BugType.FLY to BugCfg(
                    type = BugType.FLY,
                    radius = 18f,
                    speed = 120f,
                    points = 10,
                    spriteKey = "fly",
                ),
                BugType.BEE to BugCfg(
                    type = BugType.BEE,
                    radius = 22f,
                    speed = 180f,
                    points = 25,
                    spriteKey = "bee",
                ),
                BugType.BEETLE to BugCfg(
                    type = BugType.BEETLE,
                    radius = 26f,
                    speed = 70f,
                    points = 5,
                    spriteKey = "beetle",
                ),
            ),
            spawnIntervalSec = 0.8f,
            missPenalty = 5,
            maxBugs = 12,
        )
    }
}
