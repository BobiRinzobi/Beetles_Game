package com.phoenix.beetles.feature.registration.presentation.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.beetles.core.data.local.entity.UserEntity
import com.phoenix.beetles.feature.core.domain.repository.GameRepository
import com.phoenix.beetles.feature.registration.domain.usecase.GetAllUsersFioUseCase
import com.phoenix.beetles.feature.registration.domain.usecase.InsertUserUseCase
import com.phoenix.beetles.feature.registration.domain.usecase.SetActiveUserUseCase
import com.phoenix.beetles.feature.settings.domain.entity.Settings
import com.phoenix.beetles.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val insertUserUseCase: InsertUserUseCase,
    private val getAllUsersFioUseCase: GetAllUsersFioUseCase,
    private val setActiveUserUseCase: SetActiveUserUseCase,
    private val gameRepository: GameRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _state = MutableLiveData<RegistrationState>(RegistrationState.Initial)
    val state: LiveData<RegistrationState> = _state

    fun loadData() {
        _state.value = RegistrationState.Loading
        viewModelScope.launch {
            getAllUsersFioUseCase()
                .catch {
                    _state.postValue(RegistrationState.Error)
                }
                .collect { usersList ->
                    val active = usersList.find { it.isActive } ?: usersList.firstOrNull()
                    _state.postValue(RegistrationState.Content(
                        users = usersList,
                        activeUser = active,
                    ))
                }
        }
    }

    fun registerUser(user: UserEntity) {
        viewModelScope.launch {
            insertUserUseCase(user)
            setActiveUserUseCase(user)
            applyProfileSettings(user)
        }
    }

    fun setActiveUser(user: UserEntity) {
        viewModelScope.launch {
            setActiveUserUseCase(user)
            applyProfileSettings(user)
        }
    }

    private suspend fun applyProfileSettings(selectedUser: UserEntity) {
        val user = gameRepository.getActiveUser() ?: selectedUser
        val settings = user.toSettings()
            ?: settingsForDifficulty(user.difficulty ?: 1)
        settingsRepository.updateSettings(settings)
        if (user.toSettings() == null) {
            gameRepository.updateUser(user.withSettings(settings))
        }
    }

    private fun UserEntity.toSettings(): Settings? {
        if (gameSpeed == null || maxBeetlesCount == null || boostSpawn == null || roundTime == null) return null
        return Settings().copy(
            gameSpeed = gameSpeed,
            maxBeetlesCount = maxBeetlesCount,
            boostSpawn = boostSpawn,
            roundTime = roundTime,
        )
    }

    private fun UserEntity.withSettings(settings: Settings) = copy(
        gameSpeed = settings.gameSpeed,
        maxBeetlesCount = settings.maxBeetlesCount,
        boostSpawn = settings.boostSpawn,
        roundTime = settings.roundTime,
    )

    private fun settingsForDifficulty(level: Int): Settings {
        val defaults = Settings()
        val index = (level - 1).coerceIn(0, 3)

        return defaults.copy(
            gameSpeed = defaults.gameSpeedOptions.sorted()[index],
            maxBeetlesCount = defaults.maxBeetlesCountOptions.sorted()[index],
            boostSpawn = defaults.boostSpawnOptions.sorted()[index],
            roundTime = defaults.roundTimeOptions.sortedDescending()[index],
        )
    }
}
