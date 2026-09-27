package com.phoenix.beetles.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute {
    @Serializable
    data object Registration : AppRoute

    @Serializable
    data object Rules : AppRoute

    @Serializable
    data object Authors : AppRoute

    @Serializable
    data object Settings : AppRoute

    @Serializable
    data object Game : AppRoute
}
