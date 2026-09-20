package com.phoenix.beetles.feature.rules.presentation.presenter

import androidx.lifecycle.ViewModel
import com.phoenix.beetles.feature.rules.domain.repository.RulesRepository

class RulesViewModel(
    private val repository: RulesRepository
) : ViewModel() {

    fun getRules(): String {
        return repository.getRules()
    }
}