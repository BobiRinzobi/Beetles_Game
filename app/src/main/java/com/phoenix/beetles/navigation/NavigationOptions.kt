package com.phoenix.beetles.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.reflect.KClass

enum class NavigationOptions(
    val route: AppRoute,
    val title: String,
    val icon: ImageVector,
) {
    REGISTRATION(AppRoute.Registration, "Регистрация", Icons.Default.Face),
    RULES(AppRoute.Rules, "Правила", Icons.Default.Info),
    AUTHORS(AppRoute.Authors, "Авторы", Icons.Default.Person),
    SETTINGS(AppRoute.Settings, "Настройки", Icons.Default.Settings),
    GAME(AppRoute.Game, "Игра", Icons.Default.PlayArrow);

    val routeClass: KClass<out AppRoute> get() = route::class
}
