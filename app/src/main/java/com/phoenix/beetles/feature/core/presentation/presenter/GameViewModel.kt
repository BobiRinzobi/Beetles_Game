package com.phoenix.beetles.feature.core.presentation.presenter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.beetles.feature.core.domain.config.GameConfig
import com.phoenix.beetles.feature.core.domain.entity.Bounds
import com.phoenix.beetles.feature.core.domain.entity.GameWorld
import com.phoenix.beetles.feature.core.domain.entity.Position
import com.phoenix.beetles.feature.core.domain.entity.TiltForce
import com.phoenix.beetles.feature.core.domain.port.ScoreRepository
import com.phoenix.beetles.feature.core.domain.port.SfxPlayer
import com.phoenix.beetles.feature.core.domain.repository.GameRepository
import com.phoenix.beetles.feature.core.domain.usecase.AnalyzeDifficultyUseCase
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
import com.phoenix.beetles.feature.settings.domain.entity.Settings
import com.phoenix.beetles.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(
    val world: GameWorld,
    baseGameConfig: GameConfig,
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
    private val gameRepository: GameRepository,
    private val settingsRepository: SettingsRepository,
    private val analyzeDifficulty: AnalyzeDifficultyUseCase,
) : ViewModel() {

    private val baseConfig = baseGameConfig
    private var difficultyLevel = DEFAULT_DIFFICULTY_LEVEL
    private var config: GameConfig = baseConfig.withSettings(settingsRepository.getCurrentSettings())
    private var profileSettings: Settings = settingsRepository.getCurrentSettings()
    private var profileReady = false
    private var _currentTimeLeft: Float = config.roundTime.toFloat()
    private var currentTilt: TiltForce = TiltForce()
    private var goldenBugAccumulator = 0f
    var isGameFinished = false

    private val _goldPrice = MutableStateFlow(goldPriceRepository.getCachedGoldPrice())
    val goldPrice: StateFlow<GoldPrice?> = _goldPrice.asStateFlow()

    private val _uiState = MutableStateFlow<GameUiState>(
        GameUiState.Playing(score = 0, misses = 0, timeLeft = config.roundTime)
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        spawnBugs.updateConfig(config)
        spawnBonus.updateConfig(config)
        viewModelScope.launch {
            val user = gameRepository.getActiveUser()
            if (user != null) {
                difficultyLevel = (user.difficulty ?: DEFAULT_DIFFICULTY_LEVEL)
                    .coerceIn(MIN_DIFFICULTY_LEVEL, MAX_DIFFICULTY_LEVEL)
                profileSettings = user.toSettings() ?: Settings()
                config = baseConfig.withSettings(profileSettings)
                spawnBugs.updateConfig(config)
                spawnBonus.updateConfig(config)
                if (user.toSettings() == null || user.difficulty != difficultyLevel) {
                    gameRepository.updateUser(user.withSettings(profileSettings).copy(difficulty = difficultyLevel))
                }
                if (settingsRepository.getCurrentSettings() != profileSettings) {
                    settingsRepository.updateSettings(profileSettings)
                }
            }
            _currentTimeLeft = config.roundTime.toFloat()
            refreshState()
            profileReady = true
            val latestPrice = goldPriceRepository.refreshGoldPrice()
            if (latestPrice != null) _goldPrice.value = latestPrice
        }
        viewModelScope.launch {
            settingsRepository.getSettings().collect { settings ->
                if (profileReady && settings != profileSettings) {
                    profileSettings = settings
                    config = baseConfig.withSettings(settings)
                    spawnBugs.updateConfig(config)
                    spawnBonus.updateConfig(config)
                }
            }
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
        if (!profileReady || _uiState.value !is GameUiState.Playing) return
        when (handleTap.execute(position)) {
            is TapResult.Bonus -> sfxPlayer.startBugScream()
            else -> Unit
        }
        refreshState()
    }

    fun onTick(dt: Float) {
        if (!profileReady || _uiState.value !is GameUiState.Playing) return

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
        isGameFinished = false
        sfxPlayer.stopBugScream()
        config = baseConfig.withSettings(profileSettings)
        spawnBugs.updateConfig(config)
        spawnBonus.updateConfig(config)
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

    fun resetGameFinished() {
        isGameFinished = false
    }

    private fun syncScream() {
        if (world.isGravityActive) {
            sfxPlayer.startBugScream()
        } else {
            sfxPlayer.stopBugScream()
        }
    }

    private fun GameConfig.withSettings(settings: Settings): GameConfig {
        val speedMultiplier = settings.gameSpeed.coerceIn(MIN_GAME_SPEED, MAX_GAME_SPEED)
        return copy(
            bugConfig = bugConfig.mapValues { (_, bug) ->
                bug.copy(speed = bug.speed * speedMultiplier)
            },
            spawnIntervalSec = (spawnIntervalSec / speedMultiplier)
                .coerceIn(MIN_SPAWN_INTERVAL_SEC, MAX_SPAWN_INTERVAL_SEC),
            maxBugs = settings.maxBeetlesCount.coerceIn(MIN_BUG_COUNT, MAX_BUG_COUNT),
            roundTime = settings.roundTime,
            bonusIntervalSec = settings.boostSpawn.toFloat()
                .coerceIn(MIN_BONUS_INTERVAL_SEC, MAX_BONUS_INTERVAL_SEC),
        )
    }

    private suspend fun adaptDifficulty(scoresNewestFirst: List<Int>): String {
        val activeUser = gameRepository.getActiveUser() ?: return "Профиль игрока не найден."
        difficultyLevel = (activeUser.difficulty ?: DEFAULT_DIFFICULTY_LEVEL)
            .coerceIn(MIN_DIFFICULTY_LEVEL, MAX_DIFFICULTY_LEVEL)
        val user = if (activeUser.difficulty != difficultyLevel) {
            activeUser.copy(difficulty = difficultyLevel).also { gameRepository.updateUser(it) }
        } else {
            activeUser
        }
        val adjustment = analyzeDifficulty.execute(scoresNewestFirst)
        if (adjustment == 0) {
            return when {
                scoresNewestFirst.size < MIN_RESULTS_FOR_ADAPTATION ->
                    "Для адаптации сложности нужно минимум $MIN_RESULTS_FOR_ADAPTATION результата."
                else -> "Сложность не изменилась: результат изменился менее чем на 15%."
            }
        }

        val newLevel = (difficultyLevel + adjustment)
            .coerceIn(MIN_DIFFICULTY_LEVEL, MAX_DIFFICULTY_LEVEL)
        if (newLevel == difficultyLevel) {
            val limit = if (adjustment > 0) "максимум" else "минимум"
            return "Сложность не изменилась: достигнут $limit (уровень $difficultyLevel)."
        }

        val previousSettings = user.toSettings() ?: profileSettings
        val updatedSettings = previousSettings.withDifficultyStep(adjustment)
//        if (updatedSettings == previousSettings) {
//            val limit = if (adjustment > 0) "максимум" else "минимум"
//            return "Сложность не изменилась: настройки уже достигли доступного предела ($limit)."
//        }

        difficultyLevel = newLevel
        profileSettings = updatedSettings
        gameRepository.updateUser(user.withSettings(updatedSettings).copy(difficulty = difficultyLevel))
        settingsRepository.updateSettings(updatedSettings)
        config = baseConfig.withSettings(updatedSettings)
        spawnBugs.updateConfig(config)
        spawnBonus.updateConfig(config)

//        val speedDescription = "скорость ${formatSpeed(previousSettings.gameSpeed)}× → ${formatSpeed(updatedSettings.gameSpeed)}×"
//        val beetlesDescription = "максимум жуков ${previousSettings.maxBeetlesCount} → ${updatedSettings.maxBeetlesCount}"
//        val boostDescription = "интервал бонусов ${previousSettings.boostSpawn}с → ${updatedSettings.boostSpawn}с"
//        val roundDescription = "длительность раунда ${previousSettings.roundTime}с → ${updatedSettings.roundTime}с"
        val directionText = if (adjustment > 0) "повышена" else "снижена"
        return "Сложность $directionText до уровня $difficultyLevel"
//        : $speedDescription, " +
//        "$beetlesDescription, $boostDescription, $roundDescription.
    }

    private fun Settings.withDifficultyStep(direction: Int): Settings = copy(
        gameSpeed = stepOption(gameSpeedOptions, gameSpeed, direction),
        maxBeetlesCount = stepOption(maxBeetlesCountOptions, maxBeetlesCount, direction),
        boostSpawn = stepOption(boostSpawnOptions, boostSpawn, -direction),
        roundTime = stepOption(roundTimeOptions, roundTime, -direction),
    )

    private fun com.phoenix.beetles.core.data.local.entity.UserEntity.toSettings(): Settings? {
        if (gameSpeed == null || maxBeetlesCount == null || boostSpawn == null || roundTime == null) return null
        return settingsRepository.getCurrentSettings().copy(
            gameSpeed = gameSpeed,
            maxBeetlesCount = maxBeetlesCount,
            boostSpawn = boostSpawn,
            roundTime = roundTime,
        )
    }

    private fun com.phoenix.beetles.core.data.local.entity.UserEntity.withSettings(settings: Settings) = copy(
        gameSpeed = settings.gameSpeed,
        maxBeetlesCount = settings.maxBeetlesCount,
        boostSpawn = settings.boostSpawn,
        roundTime = settings.roundTime,
    )

    private fun <T : Comparable<T>> stepOption(options: List<T>, current: T, direction: Int): T {
        val sortedOptions = options.sorted()
        if (sortedOptions.isEmpty()) return current

        val currentIndex = sortedOptions.indexOf(current)
        if (currentIndex < 0) return current

        val nextIndex = (currentIndex + direction).coerceIn(sortedOptions.indices)
        return sortedOptions[nextIndex]
    }

    private fun formatSpeed(speed: Float): String =
        speed.toString().replace('.', ',')

    fun finishGame() {
        if (isGameFinished) return
        isGameFinished = true
        sfxPlayer.stopBugScream()

        val currentScore = world.score.value

        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                gameRepository.getActiveUser()
            }

            if (user != null) {
                scoreRepository.save(user.id, currentScore)
                scoreRepository.saveBest(user.id, currentScore)

                val best = scoreRepository.loadBest()
                val history = scoreRepository.loadRecentScores(user.id)
                val difficultyNotice = adaptDifficulty(history)
                val average = if (history.isNotEmpty()) {
                    history.average().toInt()
                } else {
                    0
                }

                withContext(Dispatchers.IO) {
                    saveUserRecordUseCase(newScore = currentScore)
                }

                _uiState.value = GameUiState.RegisteredGameOver(
                    score = currentScore,
                    best = best,
                    recentScores = history,
                    averageScore = average,
                    difficultyNotice = difficultyNotice,
                )
            } else {
                _uiState.value = GameUiState.GuestGameOver(
                    score = currentScore
                )

            }
        }

    }

    override fun onCleared() {
        sfxPlayer.stopBugScream()
        super.onCleared()
    }

    private companion object {
        const val GOLDEN_BUG_INTERVAL_SEC = 20f
        const val MIN_DIFFICULTY_LEVEL = 0
        const val MAX_DIFFICULTY_LEVEL = 4
        const val DEFAULT_DIFFICULTY_LEVEL = 1
        const val MIN_GAME_SPEED = 0.5f
        const val MAX_GAME_SPEED = 2.0f
        const val MIN_SPAWN_INTERVAL_SEC = 0.4f
        const val MAX_SPAWN_INTERVAL_SEC = 2.0f
        const val MIN_BUG_COUNT = 5
        const val MAX_BUG_COUNT = 30
        const val MIN_BONUS_INTERVAL_SEC = 5f
        const val MAX_BONUS_INTERVAL_SEC = 30f
        const val MIN_RESULTS_FOR_ADAPTATION = 3
    }
}
