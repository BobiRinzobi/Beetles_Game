package com.phoenix.beetles.feature.settings.domain.usecase

import com.phoenix.beetles.feature.settings.domain.entity.Settings
import com.phoenix.beetles.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class UpdateSettingsUseCase(
    val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(settings: Settings)  =
        settingsRepository.updateSettings(settings)


}