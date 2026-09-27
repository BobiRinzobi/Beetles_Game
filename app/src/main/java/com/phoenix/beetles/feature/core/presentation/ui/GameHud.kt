package com.phoenix.beetles.feature.core.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.phoenix.beetles.feature.core.presentation.presenter.GameUiState
import java.util.Locale

@Composable
fun GameHud(
    timeLeft : Float,
    onRestart: () -> Unit,
    state: GameUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Очки: ${state.score()}",
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            text = formatTime(timeLeft.toInt()),
            style = MaterialTheme.typography.titleLarge,
            color = if (timeLeft <= 10) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Промахи: ${state.misses()}",
            style = MaterialTheme.typography.titleMedium,
        )

        IconButton(
            onClick = { onRestart() }
        ) {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = "refresh"
            )
        }
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
}