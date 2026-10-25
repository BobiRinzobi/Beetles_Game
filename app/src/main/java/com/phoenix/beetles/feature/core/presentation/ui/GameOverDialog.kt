package com.phoenix.beetles.feature.core.presentation.ui

import androidx.compose.foundation.layout.Column
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
    state: GameUiState.GameOver,
    onRestart: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text("Игра окончена")
        },
        text = {
            Column {
                Text("Ваш счёт: ${state.score}")
                Text("Лучший результат: ${state.best}")
                Text("Средний результат: ${state.averageScore}")

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
        },
        confirmButton = {
            TextButton(onClick = onRestart) {
                Text("Ещё раз")
            }
        },
    )
}
