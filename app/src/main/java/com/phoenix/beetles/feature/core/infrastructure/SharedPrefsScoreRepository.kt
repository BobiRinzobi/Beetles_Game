package com.phoenix.beetles.feature.core.infrastructure

import android.content.SharedPreferences
import com.phoenix.beetles.feature.core.domain.port.ScoreRepository
import androidx.core.content.edit

class SharedPrefsScoreRepository(
    private val prefs: SharedPreferences,
) : ScoreRepository {

    override fun loadBest(): Int =
        prefs.getInt(KEY_BEST, 0)

    override fun saveBest(userId: String, score: Int) {
        if (score > loadBest()) {
            prefs.edit {
                putInt(KEY_BEST, score)
            }
        }
    }

    override fun save(userId: String, score: Int) {
        val history = prefs.getString(historyKey(userId), "")
            .orEmpty()
            .split(",")
            .filter { it.isNotBlank() }
            .mapNotNull { it.toIntOrNull() }

        val updated = (history + score).takeLast(MAX_HISTORY)

        prefs.edit {
            putString(historyKey(userId), updated.joinToString(","))
        }
    }

    override fun loadRecentScores(
        userId: String,
        limit: Int
    ): List<Int> {
        if (limit <= 0) return emptyList()

        return prefs.getString(historyKey(userId), "")
            .orEmpty()
            .split(",")
            .filter { it.isNotBlank() }
            .mapNotNull { it.toIntOrNull() }
            .takeLast(limit)
            .reversed()
    }

    private fun historyKey(userId: String) = "score_history_$userId"

    private companion object {
        const val KEY_BEST = "best_score"
        const val MAX_HISTORY = 20
    }
}
