package com.phoenix.beetles.feature.core.presentation.presenter
sealed interface GameUiState {
    data class Playing(
        val score: Int,
        val timeLeft : Int,
        val misses: Int,
        val gravityTimeLeft: Int = 0,
    ) : GameUiState
    data class Paused(val score: Int) : GameUiState
    data class GameOver(val score: Int, val best: Int) : GameUiState
}