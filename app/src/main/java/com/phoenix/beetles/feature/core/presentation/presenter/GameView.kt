package com.phoenix.beetles.feature.core.presentation.presenter

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import com.phoenix.beetles.feature.core.domain.entity.Position

class GameView(context: Context) : View(context) {

    var onTapListener: ((Position) -> Unit)? = null
    var onFrameListener: ((Canvas) -> Unit)? = null
    var onSizeListener: ((Int, Int) -> Unit)? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        onSizeListener?.invoke(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        onFrameListener?.invoke(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            onTapListener?.invoke(Position(event.x, event.y))
            return true
        }
        return super.onTouchEvent(event)
    }
}