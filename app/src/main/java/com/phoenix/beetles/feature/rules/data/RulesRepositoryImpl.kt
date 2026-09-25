package com.phoenix.beetles.feature.rules.data

import android.content.Context
import com.phoenix.beetles.R
import com.phoenix.beetles.feature.rules.domain.repository.RulesRepository

class RulesRepositoryImpl(
    private val context: Context
) : RulesRepository {

    override fun getRules(): String {
        return context.resources
            .openRawResource(R.raw.game_rules)
            .bufferedReader()
            .use { it.readText() }
    }
}