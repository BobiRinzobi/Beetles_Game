package com.phoenix.beetles.feature.core.infrastructure

import android.content.SharedPreferences
import com.phoenix.beetles.feature.core.domain.port.ScoreRepository
import androidx.core.content.edit

class SharedPrefsScoreRepository(
    private val prefs: SharedPreferences,
) : ScoreRepository {

    override fun loadBest(): Int = prefs.getInt(KEY_BEST, 0)

    override fun saveBest(score: Int) {
        if (score > loadBest()) {
            prefs.edit { putInt(KEY_BEST, score) }
        }
    }

    private companion object {
        const val KEY_BEST = "best_score"
    }
}