package com.phoenix.beetles.feature.records.presentation.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.beetles.core.data.local.entity.UserEntity
import com.phoenix.beetles.feature.records.domain.usecase.GetLeaderboardUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class RecordsViewModel(
    getLeaderboardUseCase: GetLeaderboardUseCase
) : ViewModel() {

    val users: StateFlow<List<UserEntity>> = getLeaderboardUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}