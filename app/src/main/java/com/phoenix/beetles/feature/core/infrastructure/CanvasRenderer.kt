package com.phoenix.beetles.feature.core.infrastructure

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.phoenix.beetles.feature.core.domain.entity.Bonus
import com.phoenix.beetles.feature.core.domain.entity.Bug
import com.phoenix.beetles.feature.core.domain.entity.BugType
import com.phoenix.beetles.feature.core.domain.port.Renderer

class CanvasRenderer(
    private val canvas: Canvas,
    private val width: Int,
    private val height: Int,
    private val bugBitmaps: Map<BugType, Bitmap>,
    private val bonusBitmap: Bitmap? = null,
) : Renderer {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val dstRect = RectF()

    override fun clear() {
        canvas.drawColor(Color.WHITE)
    }
    override fun drawBug(bug: Bug) {
        val bitmap = bugBitmaps[bug.type] ?: return
        val radius = bug.config.radius

        canvas.save()

        val angleInDegrees = Math.toDegrees(bug.angle.toDouble()).toFloat()
        canvas.rotate(angleInDegrees + 90f, bug.position.x, bug.position.y)

        dstRect.set(
            bug.position.x - radius,
            bug.position.y - radius,
            bug.position.x + radius,
            bug.position.y + radius
        )

        canvas.drawBitmap(bitmap, null, dstRect, paint)

        canvas.restore()
    }

    override fun drawBonus(bonus: Bonus) {
        val radius = bonus.radius
        dstRect.set(
            bonus.position.x - radius,
            bonus.position.y - radius,
            bonus.position.x + radius,
            bonus.position.y + radius,
        )
        val bitmap = bonusBitmap
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, null, dstRect, paint)
            return
        }
        paint.color = Color.rgb(255, 193, 7)
        canvas.drawCircle(bonus.position.x, bonus.position.y, radius, paint)
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
}