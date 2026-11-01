package com.phoenix.beetles.feature.core.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.phoenix.beetles.feature.core.presentation.presenter.GameUiState


@Composable
fun GameOverDialog(
    state: GameUiState.RegisteredGameOver,
    onRestart: () -> Unit,
) {
    GameOverDialog(
        score = state.score,
        onRestart = onRestart,
    ) {
        Text("Лучший результат: ${state.best}")
        Text("Средний результат: ${state.averageScore}")

        Spacer(modifier = Modifier.height(12.dp))
        Text(state.difficultyNotice)

        Spacer(modifier = Modifier.height(12.dp))
        Text("Последние игры:")

        if (state.recentScores.isEmpty()) {
            Text("История пока пуста")
        } else {
            state.recentScores.forEachIndexed { index, score ->
                Text("${index + 1}. $score очков")
            }
        }
    }
}

@Composable
fun GameOverDialog(
    state: GameUiState.GuestGameOver,
    onRestart: () -> Unit,
) {
    GameOverDialog(
        score = state.score,
        onRestart = onRestart,
    )
}

@Composable
private fun GameOverDialog(
    score: Int,
    onRestart: () -> Unit,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text("Игра окончена")
        },
        text = {
            Column {
                Text("Ваш счёт: $score")
                content()
            }
        },
        confirmButton = {
            TextButton(onClick = onRestart) {
                Text("Ещё раз")
            }
        },
    )
}
