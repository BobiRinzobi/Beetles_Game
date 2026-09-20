package com.phoenix.beetles.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.phoenix.beetles.feature.authors.navigation.AuthorsRoute
import com.phoenix.beetles.feature.registration.navigation.RegistrationRoute
import com.phoenix.beetles.feature.rules.navigation.RulesRoute
import com.phoenix.beetles.feature.settings.navigation.SettingRoule
import kotlin.reflect.KClass


enum class NavigationOptions(
    val routeClass: KClass<*>,
    val routeObject: Any,
    val title: String,
    val icon: ImageVector
) {
    REGISTRATION(RegistrationRoute::class, RegistrationRoute, "Регистрация", Icons.Default.Face),
    RULES(RulesRoute::class, RulesRoute, "Правила", Icons.Default.Info),
    AUTHORS(AuthorsRoute::class, AuthorsRoute, "Авторы", Icons.Default.Person),
    SETTINGS(SettingRoule::class, SettingRoule, "Настройки", Icons.Default.Settings)
}