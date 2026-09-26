package com.phoenix.beetles.feature.core.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.phoenix.beetles.feature.core.infrastructure.CanvasRenderer
import com.phoenix.beetles.feature.core.presentation.presenter.GameHud
import com.phoenix.beetles.feature.core.presentation.presenter.GameLoop
import com.phoenix.beetles.feature.core.presentation.presenter.GameUiState
import com.phoenix.beetles.feature.core.presentation.presenter.GameView
import com.phoenix.beetles.feature.core.presentation.presenter.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    var gameView by remember { mutableStateOf<GameView?>(null) }
    val gameLoop = remember(viewModel) {
        GameLoop(onTick = { dt ->
            viewModel.onTick(dt)
            gameView?.invalidate()
        })
    }

    DisposableEffect(gameLoop) {
        gameLoop.start()
        onDispose { gameLoop.stop() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                GameView(context).apply {
                    gameView = this
                    onSizeListener = { width, height ->
                        viewModel.onSurfaceReady(width.toFloat(), height.toFloat())
                    }
                    onTapListener = { position -> viewModel.onTap(position) }
                    onFrameListener = { canvas ->
                        val renderer = CanvasRenderer(canvas, width, height)
                        renderer.clear()
                        viewModel.world.aliveBugs().forEach { renderer.drawBug(it) }
                        renderer.drawScore(
                            viewModel.world.score.value,
                            viewModel.world.score.misses,
                        )
                    }
                }
            }
        )

        GameHud(
            state = uiState,
            modifier = Modifier.align(Alignment.TopStart)
        )

        val gameOverState = uiState as? GameUiState.GameOver
        if (gameOverState != null) {
            GameOverDialog(
                state = gameOverState,
                onRestart = { viewModel.onRestart() },
            )
        }
    }
}
