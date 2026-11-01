package com.phoenix.beetles.feature.core.presentation.presenter
sealed interface GameUiState {
    data class Playing(
        val score: Int,
        val timeLeft : Int,
        val misses: Int,
        val gravityTimeLeft: Int = 0,
    ) : GameUiState
    data class Paused(val score: Int) : GameUiState
    data class GuestGameOver(val score: Int) : GameUiState
    data class RegisteredGameOver(
        val score: Int,
        val best: Int,
        val recentScores: List<Int>,
        val averageScore: Int,
        val difficultyNotice: String,
    ) : GameUiState
}
