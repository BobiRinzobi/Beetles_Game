package com.phoenix.beetles.feature.core.presentation.presenter

import androidx.lifecycle.ViewModel
import com.phoenix.beetles.feature.core.domain.entity.Bounds
import com.phoenix.beetles.feature.core.domain.entity.GameWorld
import com.phoenix.beetles.feature.core.domain.entity.Position
import com.phoenix.beetles.feature.core.domain.port.ScoreRepository
import com.phoenix.beetles.feature.core.domain.usecase.HandleTapUseCase
import com.phoenix.beetles.feature.core.domain.usecase.RestartGameUseCase
import com.phoenix.beetles.feature.core.domain.usecase.SpawnBugsUseCase
import com.phoenix.beetles.feature.core.domain.usecase.UpdateWorldUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(
    val world: GameWorld,
    private val handleTap: HandleTapUseCase,
    private val spawnBugs: SpawnBugsUseCase,
    val updateWorld: UpdateWorldUseCase,
    private val restartGame: RestartGameUseCase,
    private val scoreRepository: ScoreRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameUiState>(
        GameUiState.Playing(score = 0, misses = 0)
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun onSurfaceReady(width: Float, height: Float) {
        if (width <= 0f || height <= 0f) return
        world.bounds = Bounds(width, height)
    }

    fun onTap(position: Position) {
        if (_uiState.value !is GameUiState.Playing) return
        handleTap.execute(position)
        refreshState()
    }

    fun onTick(dt: Float) {
        if (_uiState.value !is GameUiState.Playing) return
        spawnBugs.tick(dt)
        updateWorld.execute(dt)
    }

    fun onRestart() {
        restartGame.execute()
        _uiState.value = GameUiState.Playing(score = 0, misses = 0)
    }

    private fun refreshState() {
        _uiState.value = GameUiState.Playing(
            score = world.score.value,
            misses = world.score.misses,
        )
    }

    fun finishGame() {
        val best = maxOf(scoreRepository.loadBest(), world.score.value)
        scoreRepository.saveBest(world.score.value)
        _uiState.value = GameUiState.GameOver(
            score = world.score.value,
            best = best,
        )
    }
}
