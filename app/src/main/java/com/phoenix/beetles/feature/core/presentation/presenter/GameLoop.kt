package com.phoenix.beetles.feature.core.presentation.presenter

import android.view.Choreographer

class GameLoop(
    private val onTick: (dtSec: Float) -> Unit,
) : Choreographer.FrameCallback {

    private var lastFrameNs = 0L
    private var running = false

    fun start() {
        if (running) return
        running = true
        lastFrameNs = 0L
        Choreographer.getInstance().postFrameCallback(this)
    }

    fun stop() {
        running = false
        Choreographer.getInstance().removeFrameCallback(this)
    }

    override fun doFrame(frameTimeNanos: Long) {
        if (!running) return

        if (lastFrameNs == 0L) lastFrameNs = frameTimeNanos
        val dtNs = frameTimeNanos - lastFrameNs
        lastFrameNs = frameTimeNanos

        val dtSec = (dtNs / 1_000_000_000.0).toFloat().coerceAtMost(0.1f)
        onTick(dtSec)

        Choreographer.getInstance().postFrameCallback(this)
    }
}