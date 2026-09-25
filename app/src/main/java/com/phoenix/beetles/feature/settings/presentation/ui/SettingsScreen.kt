package com.phoenix.beetles.feature.settings.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.phoenix.beetles.feature.settings.presentation.presenter.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val settings by viewModel.settingsState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Настройки игры",
            style = MaterialTheme.typography.headlineMedium
        )

        SettingsSection(title = "Скорость игры") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                settings.gameSpeedOptions.forEach { speed ->
                    FilterChip(
                        selected = settings.gameSpeed == speed,
                        onClick = {
                            viewModel.updateSettings(settings.copy(gameSpeed = speed))
                        },
                        label = { Text("${speed}x") }
                    )
                }
            }
        }

        SettingsSection(title = "Макс. тараканов") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                settings.maxBeetlesCountOptions.forEach { count ->
                    FilterChip(
                        selected = settings.maxBeetlesCount == count,
                        onClick = {
                            viewModel.updateSettings(settings.copy(maxBeetlesCount = count))
                        },
                        label = { Text("$count") }
                    )
                }
            }
        }

        SettingsSection(title = "интервал появления бонусов") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                settings.boostSpawnOptions.forEach { boost ->
                    FilterChip(
                        selected = settings.boostSpawn == boost,
                        onClick = {
                            viewModel.updateSettings(settings.copy(boostSpawn = boost))
                        },
                        label = { Text("${boost}с") }
                    )
                }
            }
        }

        SettingsSection(title = "Длительность раунда") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                settings.roundTimeOptions.forEach { time ->
                    FilterChip(
                        selected = settings.roundTime == time,
                        onClick = {
                            viewModel.updateSettings(settings.copy(roundTime = time))
                        },
                        label = { Text("${time}с") }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        content()
    }
}