package com.phoenix.beetles.feature.core.infrastructure

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.phoenix.beetles.feature.core.domain.entity.Bug
import com.phoenix.beetles.feature.core.domain.port.Renderer

class CanvasRenderer(
    private val canvas: Canvas,
    private val width: Int,
    private val height: Int,
) : Renderer {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun clear() {
        canvas.drawColor(Color.WHITE)
    }

    override fun drawBug(bug: Bug) {
        paint.color = colorFor(bug)
        canvas.drawCircle(
            bug.position.x,
            bug.position.y,
            bug.config.radius,
            paint,
        )
    }

    override fun drawScore(score: Int, misses: Int) {
        paint.color = Color.BLACK
        paint.textSize = 40f
        canvas.drawText("Score: $score  Misses: $misses", 30f, 60f, paint)
    }

    override fun drawGameOver(finalScore: Int, best: Int) {
        paint.color = Color.BLACK
        paint.textSize = 60f
        canvas.drawText("Game Over", width / 4f, height / 2f - 60f, paint)
        paint.textSize = 40f
        canvas.drawText("Score: $finalScore", width / 4f, height / 2f, paint)
        canvas.drawText("Best: $best", width / 4f, height / 2f + 60f, paint)
    }

    private fun colorFor(bug: Bug): Int = when (bug.type) {
        com.phoenix.beetles.feature.core.domain.entity.BugType.FLY -> Color.DKGRAY
        com.phoenix.beetles.feature.core.domain.entity.BugType.BEE -> Color.rgb(255, 200, 0)
        com.phoenix.beetles.feature.core.domain.entity.BugType.BEETLE -> Color.rgb(100, 150, 60)
    }
}