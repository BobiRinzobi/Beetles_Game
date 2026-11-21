package com.phoenix.beetles.feature.settings.domain.repository

import com.phoenix.beetles.feature.settings.domain.entity.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettings(): Flow<Settings>

    fun getCurrentSettings(): Settings

    fun updateSettings(settings: Settings)
}
