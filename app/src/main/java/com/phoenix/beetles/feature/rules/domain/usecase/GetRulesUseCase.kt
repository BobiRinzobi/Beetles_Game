package com.phoenix.beetles.feature.rules.domain.usecase

import com.phoenix.beetles.feature.rules.domain.repository.RulesRepository

class GetRulesUseCase(
    private val repository: RulesRepository
) {
    operator fun invoke() : String =
        repository.getRules()
}