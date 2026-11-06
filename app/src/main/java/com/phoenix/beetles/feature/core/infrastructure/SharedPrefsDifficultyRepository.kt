package com.phoenix.beetles.feature.core.infrastructure

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.phoenix.beetles.feature.core.domain.port.DifficultyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedPrefsDifficultyRepository(context: Context) : DifficultyRepository {
    private val preferences: SharedPreferences = context.applicationContext
        .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val _level = MutableStateFlow(preferences.getInt(KEY_LEVEL, DEFAULT_LEVEL))

    override fun getLevel(): Int = _level.value

    override fun observeLevel(): StateFlow<Int> = _level.asStateFlow()

    override fun saveLevel(level: Int) {
        preferences.edit { putInt(KEY_LEVEL, level) }
        _level.value = level
    }

    private companion object {
        const val PREFERENCES_NAME = "adaptive_difficulty"
        const val KEY_LEVEL = "level"
        const val DEFAULT_LEVEL = 1
    }
}
