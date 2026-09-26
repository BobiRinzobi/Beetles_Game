package com.phoenix.beetles.feature.core.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.phoenix.beetles.feature.core.presentation.presenter.GameUiState

@Composable
fun GameOverDialog(
    state: GameUiState.GameOver,
    onRestart: () -> Unit,
//    onExit: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Игра окончена") },
        text = {
            Column {
                Text("Ваш счёт: ${state.score}")
                Text("Лучший результат: ${state.best}")
            }
        },
        confirmButton = {
            TextButton(onClick = onRestart) { Text("Ещё раз") }
        },
//        dismissButton = {
//            TextButton(onClick = onExit) { Text("В настройки") }
//        },
    )
}