package com.phoenix.beetles.feature.core.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.beetles.feature.core.domain.config.GameConfig
import com.phoenix.beetles.feature.core.domain.entity.Bounds
import com.phoenix.beetles.feature.core.domain.entity.GameWorld
import com.phoenix.beetles.feature.core.domain.entity.Position
import com.phoenix.beetles.feature.core.domain.port.ScoreRepository
import com.phoenix.beetles.feature.core.domain.usecase.HandleTapUseCase
import com.phoenix.beetles.feature.core.domain.usecase.RestartGameUseCase
import com.phoenix.beetles.feature.core.domain.usecase.SaveUserRecordUseCase
import com.phoenix.beetles.feature.core.domain.usecase.SpawnBugsUseCase
import com.phoenix.beetles.feature.core.domain.usecase.UpdateWorldUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    val world: GameWorld,
    private val config: GameConfig,
    private val handleTap: HandleTapUseCase,
    private val spawnBugs: SpawnBugsUseCase,
    val updateWorld: UpdateWorldUseCase,
    private val restartGame: RestartGameUseCase,
    private val scoreRepository: ScoreRepository,
    private val saveUserRecordUseCase: SaveUserRecordUseCase
) : ViewModel() {

    private var _currentTimeLeft: Float = config.roundTime.toFloat()

    private val _uiState = MutableStateFlow<GameUiState>(
        GameUiState.Playing(score = 0, misses = 0, timeLeft = config.roundTime)
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

        _currentTimeLeft -= dt

        if (_currentTimeLeft <= 0f) {
            _currentTimeLeft = 0f
            refreshState()
            finishGame()
            return
        }

        spawnBugs.tick(dt)
        updateWorld.execute(dt)
        refreshState()
    }

    fun onRestart() {
        restartGame.execute()
        _currentTimeLeft = config.roundTime.toFloat()
        refreshState()
    }

    private fun refreshState() {
        _uiState.value = GameUiState.Playing(
            score = world.score.value,
            misses = world.score.misses,
            timeLeft = _currentTimeLeft.toInt()
        )
    }

    fun finishGame() {
        val currentScore = world.score.value
        val best = maxOf(scoreRepository.loadBest(), currentScore)
        scoreRepository.saveBest(currentScore)

        viewModelScope.launch(Dispatchers.IO) {
            saveUserRecordUseCase(newScore = currentScore)
        }

        _uiState.value = GameUiState.GameOver(
            score = currentScore,
            best = best,
        )
    }
}