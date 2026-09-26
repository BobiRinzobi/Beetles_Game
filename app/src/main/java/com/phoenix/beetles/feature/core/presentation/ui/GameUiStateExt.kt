package com.phoenix.beetles.feature.core.presentation.ui

import com.phoenix.beetles.feature.core.presentation.presenter.GameUiState

fun GameUiState.score(): Int = when (this) {
    is GameUiState.Playing -> score
    is GameUiState.Paused -> score
    is GameUiState.GameOver -> score
}

fun GameUiState.misses(): Int = when (this) {
    is GameUiState.Playing -> misses
    else -> 0
}