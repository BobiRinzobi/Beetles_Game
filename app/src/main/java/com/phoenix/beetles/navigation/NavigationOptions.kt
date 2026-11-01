package com.phoenix.beetles.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationOptions(
    val route: AppRoute,
    val title: String,
    val icon: ImageVector,
) {
    REGISTRATION(AppRoute.Registration, "Регистрация", Icons.Default.Face),
//    GAME(AppRoute.Game, "Игра", Icons.Default.PlayArrow),
    RECORDS(AppRoute.Records, "Рекорды", Icons.Default.Star),
    RULES(AppRoute.Rules, "Правила", Icons.Default.Info),
    SETTINGS(AppRoute.Settings, "Настройки", Icons.Default.Settings),
    AUTHORS(AppRoute.Authors, "Авторы", Icons.Default.Person)
}

enum class BottomBarItem(
    val route: AppRoute,
    val title: String,
    val icon: ImageVector
) {
//    REGISTRATION(AppRoute.Registration, "Регистрация", Icons.Default.Face),
    GAME(AppRoute.Game, "Игра", Icons.Default.PlayArrow),
    MENU(AppRoute.Menu, "Меню", Icons.Default.Menu)
}