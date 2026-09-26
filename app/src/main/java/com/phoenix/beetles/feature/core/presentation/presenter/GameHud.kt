package com.phoenix.beetles.feature.core.presentation.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.phoenix.beetles.feature.core.presentation.ui.misses
import com.phoenix.beetles.feature.core.presentation.ui.score

@Composable
fun GameHud(
    state: GameUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Очки: ${state.score()}",
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = "Промахи: ${state.misses()}",
            style = MaterialTheme.typography.titleMedium,
        )
    }
}