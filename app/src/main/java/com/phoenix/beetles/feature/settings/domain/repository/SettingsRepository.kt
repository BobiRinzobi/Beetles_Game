package com.phoenix.beetles.feature.settings.domain.repository

import com.phoenix.beetles.feature.settings.domain.entity.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettings(): Flow<Settings>

    suspend fun updateSettings(settings: Settings)
}