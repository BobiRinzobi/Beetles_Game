package com.phoenix.beetles.feature.settings.data.repository

import com.phoenix.beetles.feature.settings.domain.entity.Settings
import com.phoenix.beetles.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl : SettingsRepository {

    private val _settingsState = MutableStateFlow(Settings())

    override fun getSettings(): Flow<Settings> {
        return _settingsState.asStateFlow()
    }

    override suspend fun updateSettings(settings: Settings) {
        _settingsState.value = settings
    }
}