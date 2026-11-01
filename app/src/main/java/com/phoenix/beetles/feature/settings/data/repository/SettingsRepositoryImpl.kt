package com.phoenix.beetles.feature.settings.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.phoenix.beetles.feature.settings.domain.entity.Settings
import com.phoenix.beetles.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    private val preferences: SharedPreferences = context.applicationContext
        .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private val _settingsState = MutableStateFlow(readSettings())

    override fun getSettings(): Flow<Settings> {
        return _settingsState.asStateFlow()
    }

    override fun getCurrentSettings(): Settings = _settingsState.value

    override suspend fun updateSettings(settings: Settings) {
        preferences.edit {
            putFloat(KEY_GAME_SPEED, settings.gameSpeed)
            putInt(KEY_MAX_BEETLES_COUNT, settings.maxBeetlesCount)
            putInt(KEY_BOOST_SPAWN, settings.boostSpawn)
            putInt(KEY_ROUND_TIME, settings.roundTime)
        }
        _settingsState.value = settings
    }

    private fun readSettings() = Settings(
        gameSpeed = preferences.getFloat(KEY_GAME_SPEED, DEFAULT_GAME_SPEED),
        maxBeetlesCount = preferences.getInt(KEY_MAX_BEETLES_COUNT, DEFAULT_MAX_BEETLES_COUNT),
        boostSpawn = preferences.getInt(KEY_BOOST_SPAWN, DEFAULT_BOOST_SPAWN),
        roundTime = preferences.getInt(KEY_ROUND_TIME, DEFAULT_ROUND_TIME),
    )

    private companion object {
        const val PREFERENCES_NAME = "game_settings"
        const val KEY_GAME_SPEED = "game_speed"
        const val KEY_MAX_BEETLES_COUNT = "max_beetles_count"
        const val KEY_BOOST_SPAWN = "boost_spawn"
        const val KEY_ROUND_TIME = "round_time"

        const val DEFAULT_GAME_SPEED = 1.0f
        const val DEFAULT_MAX_BEETLES_COUNT = 10
        const val DEFAULT_BOOST_SPAWN = 15
        const val DEFAULT_ROUND_TIME = 60
    }
}
