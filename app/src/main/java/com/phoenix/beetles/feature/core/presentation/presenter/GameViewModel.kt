package com.phoenix.beetles.feature.core.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.beetles.feature.core.domain.config.GameConfig
import com.phoenix.beetles.feature.core.domain.entity.Bounds
import com.phoenix.beetles.feature.core.domain.entity.GameWorld
import com.phoenix.beetles.feature.core.domain.entity.Position
import com.phoenix.beetles.feature.core.domain.entity.TiltForce
import com.phoenix.beetles.feature.core.domain.port.ScoreRepository
import com.phoenix.beetles.feature.core.domain.port.SfxPlayer
import com.phoenix.beetles.feature.core.domain.usecase.HandleTapUseCase
import com.phoenix.beetles.feature.core.domain.usecase.RestartGameUseCase
import com.phoenix.beetles.feature.core.domain.usecase.SaveUserRecordUseCase
import com.phoenix.beetles.feature.core.domain.usecase.SpawnBonusUseCase
import com.phoenix.beetles.feature.core.domain.usecase.SpawnBugsUseCase
import com.phoenix.beetles.feature.core.domain.usecase.TapResult
import com.phoenix.beetles.feature.core.domain.usecase.UpdateWorldUseCase
import com.phoenix.beetles.feature.gold.domain.model.GoldPrice
import com.phoenix.beetles.feature.gold.domain.repository.GoldRepository
import com.phoenix.beetles.feature.gold.domain.usecase.SpawnGoldenBugUseCase
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
    private val spawnBonus: SpawnBonusUseCase,
    private val spawnGoldenBug: SpawnGoldenBugUseCase,
    private val goldPriceRepository: GoldRepository,
    private val restartGame: RestartGameUseCase,
    private val scoreRepository: ScoreRepository,
    private val saveUserRecordUseCase: SaveUserRecordUseCase,
    private val sfxPlayer: SfxPlayer,
) : ViewModel() {

    private var _currentTimeLeft: Float = config.roundTime.toFloat()
    private var currentTilt: TiltForce = TiltForce()
    private var goldenBugAccumulator = 0f

    private val _goldPrice = MutableStateFlow(goldPriceRepository.getCachedGoldPrice())
    val goldPrice: StateFlow<GoldPrice?> = _goldPrice.asStateFlow()

    private val _uiState = MutableStateFlow<GameUiState>(
        GameUiState.Playing(score = 0, misses = 0, timeLeft = config.roundTime)
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val latestPrice = goldPriceRepository.refreshGoldPrice()
            if (latestPrice != null) _goldPrice.value = latestPrice
        }
    }

    fun onSurfaceReady(width: Float, height: Float) {
        if (width <= 0f || height <= 0f) return
        world.bounds = Bounds(width, height)
    }

    fun onTilt(ax: Float, ay: Float) {
        currentTilt = TiltForce(ax = ax, ay = ay)
    }

    fun onTap(position: Position) {
        if (_uiState.value !is GameUiState.Playing) return
        when (handleTap.execute(position)) {
            is TapResult.Bonus -> sfxPlayer.startBugScream()
            else -> Unit
        }
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
        spawnBonus.tick(dt)
        goldenBugAccumulator += dt
        while (goldenBugAccumulator >= GOLDEN_BUG_INTERVAL_SEC) {
            goldenBugAccumulator -= GOLDEN_BUG_INTERVAL_SEC
            val reward = _goldPrice.value?.bonusPoints ?: GoldPrice.FALLBACK_BONUS_POINTS
            spawnGoldenBug.execute(reward)
        }
        updateWorld.execute(dt, currentTilt)
        world.tickGravity(dt)
        syncScream()
        refreshState()
    }

    fun onScreenHidden() {
        sfxPlayer.stopBugScream()
    }

    fun onRestart() {
        sfxPlayer.stopBugScream()
        restartGame.execute()
        _currentTimeLeft = config.roundTime.toFloat()
        goldenBugAccumulator = 0f
        refreshState()
    }

    private fun refreshState() {
        _uiState.value = GameUiState.Playing(
            score = world.score.value,
            misses = world.score.misses,
            timeLeft = _currentTimeLeft.toInt(),
            gravityTimeLeft = world.gravityTimeLeft.toInt(),
        )
    }

    fun finishGame() {
        sfxPlayer.stopBugScream()
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

    private fun syncScream() {
        if (world.isGravityActive) {
            sfxPlayer.startBugScream()
        } else {
            sfxPlayer.stopBugScream()
        }
    }

    override fun onCleared() {
        sfxPlayer.stopBugScream()
        super.onCleared()
    }

    private companion object {
        const val GOLDEN_BUG_INTERVAL_SEC = 20f
    }
}
