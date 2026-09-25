package com.phoenix.beetles.feature.rules.presentation.presenter

import androidx.lifecycle.ViewModel
import com.phoenix.beetles.feature.rules.domain.repository.RulesRepository
import com.phoenix.beetles.feature.rules.domain.usecase.GetRulesUseCase

class RulesViewModel(
    private val getRulesUseCase: GetRulesUseCase
) : ViewModel() {

    fun getRules(): String {
        return getRulesUseCase()
    }
}