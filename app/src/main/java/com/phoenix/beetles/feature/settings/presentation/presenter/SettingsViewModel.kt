package com.phoenix.beetles.feature.settings.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.beetles.feature.core.domain.port.DifficultyRepository
import com.phoenix.beetles.feature.core.domain.repository.GameRepository
import com.phoenix.beetles.feature.settings.domain.entity.Settings
import com.phoenix.beetles.feature.settings.domain.repository.SettingsRepository
import com.phoenix.beetles.feature.settings.domain.usecase.GetSettingsUseCase
import com.phoenix.beetles.feature.settings.domain.usecase.UpdateSettingsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val gameRepository: GameRepository,
    private val settingsRepository: SettingsRepository,
    private val difficultyRepository: DifficultyRepository,
) : ViewModel() {

    val settingsState: StateFlow<Settings> = getSettingsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Settings()
        )

    private val _adaptiveDifficultyLevel = kotlinx.coroutines.flow.MutableStateFlow(0)
    val adaptiveDifficultyLevel: StateFlow<Int> = _adaptiveDifficultyLevel

    init {
        viewModelScope.launch {
            val user = gameRepository.getActiveUser()
            if (user == null) {
                difficultyRepository.observeLevel().collect { level ->
                    _adaptiveDifficultyLevel.value = level
                }
                return@launch
            }
            _adaptiveDifficultyLevel.value = user.difficulty ?: 0
            val profileSettings = user.toSettings() ?: Settings()
            settingsRepository.updateSettings(profileSettings)
            if (user.toSettings() == null) {
                gameRepository.updateUser(user.withSettings(profileSettings))
            }
        }
    }

    fun updateSettings(newSettings: Settings) {
        viewModelScope.launch {
            updateSettingsUseCase(newSettings)
            val user = gameRepository.getActiveUser()
            if (user == null) {
                difficultyRepository.saveLevel(0)
                _adaptiveDifficultyLevel.value = 0
                return@launch
            }
            gameRepository.updateUser(user.copy(
                difficulty = 0,
                gameSpeed = newSettings.gameSpeed,
                maxBeetlesCount = newSettings.maxBeetlesCount,
                boostSpawn = newSettings.boostSpawn,
                roundTime = newSettings.roundTime,
            ))
            _adaptiveDifficultyLevel.value = 0
        }
    }

    private fun com.phoenix.beetles.core.data.local.entity.UserEntity.toSettings(): Settings? {
        if (gameSpeed == null || maxBeetlesCount == null || boostSpawn == null || roundTime == null) return null
        return Settings().copy(
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
}
