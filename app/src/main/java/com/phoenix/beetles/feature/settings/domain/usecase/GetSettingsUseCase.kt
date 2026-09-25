package com.phoenix.beetles.feature.settings.domain.usecase

import com.phoenix.beetles.feature.settings.domain.entity.Settings
import com.phoenix.beetles.feature.settings.domain.repository.SettingsRepository

class GetSettingsUseCase(
    val settingsRepository: SettingsRepository
) {

    operator fun invoke() =
        settingsRepository.getSettings()

}