package com.phoenix.beetles.feature.core.presentation.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.phoenix.beetles.R
import com.phoenix.beetles.feature.core.domain.entity.BugType
import com.phoenix.beetles.feature.core.infrastructure.CanvasRenderer
import com.phoenix.beetles.feature.core.presentation.presenter.GameLoop
import com.phoenix.beetles.feature.core.presentation.presenter.GameUiState
import com.phoenix.beetles.feature.core.presentation.presenter.GameView
import com.phoenix.beetles.feature.core.presentation.presenter.GameViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun GameScreen(
    viewModel: GameViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val goldPrice by viewModel.goldPrice.collectAsState()
    var gameView by remember { mutableStateOf<GameView?>(null) }

    val context = LocalContext.current

    val bugBitmaps = remember(context) {
        val res = context.resources
        mapOf(
            BugType.FLY to BitmapFactory.decodeResource(res, R.drawable.beetle),
            BugType.BEE to BitmapFactory.decodeResource(res, R.drawable.bee),
            BugType.BEETLE to BitmapFactory.decodeResource(res, R.drawable.cockroach),
            BugType.GOLD_BEETLE to BitmapFactory.decodeResource(res, R.drawable.gold_cockroach),
        )
    }

    val bonusBitmap = remember(context) {
        ContextCompat.getDrawable(context, R.drawable.ic_bonus)?.toBitmap(256, 256)
    }

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

    DisposableEffect(viewModel, context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
            ?: sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                viewModel.onTilt(event.values[0], event.values[1])
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        sensor?.let {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_GAME)
        }
        onDispose {
            sensorManager.unregisterListener(listener)
            viewModel.onScreenHidden()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { viewContext ->
                GameView(viewContext).apply {
                    gameView = this
                    onSizeListener = { width, height ->
                        viewModel.onSurfaceReady(width.toFloat(), height.toFloat())
                    }
                    onTapListener = { position -> viewModel.onTap(position) }
                    onFrameListener = { canvas ->
                        val renderer = CanvasRenderer(
                            canvas = canvas,
                            width = width,
                            height = height,
                            bugBitmaps = bugBitmaps,
                            bonusBitmap = bonusBitmap,
                        )
                        renderer.clear()
                        viewModel.world.bonus?.let { renderer.drawBonus(it) }
                        viewModel.world.aliveBugs().forEach { renderer.drawBug(it) }
                    }
                }
            }
        )

        val currentTime = (uiState as? GameUiState.Playing)?.timeLeft ?: 0

        GameHud(
            timeLeft = currentTime.toFloat(),
            onRestart = { viewModel.onRestart() },
            state = uiState,
            modifier = Modifier.align(Alignment.TopStart)
        )

        Text(
            text = goldPrice?.let {
                "Золото ЦБ: ${formatGoldPrice(it.pricePerGram)} ₽/г · золотой таракан +${it.bonusPoints}"
            } ?: "Золото ЦБ: загрузка курса…",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp, start = 16.dp, end = 16.dp),
        )

        val gravityLeft = (uiState as? GameUiState.Playing)?.gravityTimeLeft ?: 0
        if (gravityLeft > 0) {
            Text(
                text = "Наклон: ${gravityLeft}с",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 88.dp)
            )
        }

        when (val gameOverState = uiState) {
            is GameUiState.GuestGameOver -> {
                GameOverDialog(
                    state = gameOverState,
                    onRestart = { viewModel.onRestart() },
                )
            }
            is GameUiState.RegisteredGameOver -> {
                GameOverDialog(
                    state = gameOverState,
                    onRestart = { viewModel.onRestart() },
                )
            }
            else -> Unit
        }
    }
}
private fun formatGoldPrice(value: Double): String =
    NumberFormat.getNumberInstance(Locale("ru", "RU")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }.format(value)
