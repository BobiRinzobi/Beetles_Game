package com.phoenix.beetles.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.phoenix.beetles.navigation.AppRoute
import com.phoenix.beetles.navigation.BottomBarItem
import com.phoenix.beetles.navigation.NavigationPanel
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val bottomItems = BottomBarItem.entries
    val pagerState = rememberPagerState(initialPage = 0) { bottomItems.size }
    val coroutineScope = rememberCoroutineScope()

    var activeMenuRoute by remember { mutableStateOf<AppRoute>(AppRoute.Menu) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                            if (item == BottomBarItem.MENU) {
                                activeMenuRoute = AppRoute.Menu
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            val currentRoute = when (bottomItems[page]) {
                BottomBarItem.REGISTRATION -> AppRoute.Registration
                BottomBarItem.GAME -> AppRoute.Game
                BottomBarItem.MENU -> activeMenuRoute
            }

            NavigationPanel(
                route = currentRoute,
                onNavigateTo = { selectedRoute ->
                    when (selectedRoute) {
                        AppRoute.Registration -> {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(BottomBarItem.REGISTRATION.ordinal)
                            }
                            activeMenuRoute = AppRoute.Menu
                        }
                        AppRoute.Game -> {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(BottomBarItem.GAME.ordinal)
                            }
                            activeMenuRoute = AppRoute.Menu
                        }
                        else -> {
                            activeMenuRoute = selectedRoute
                        }
                    }
                }
            )
        }
    }
}