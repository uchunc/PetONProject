package com.woo.peton.features.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.woo.peton.core.ui.navigation.HomeNavigationRoute.HomeScreen
import com.woo.peton.features.home.ui.screen.HomeTabScreen

fun NavGraphBuilder.homeNavBuilder(
    navController: NavController,
    onNavigateToPetDetail: (String) -> Unit,
) {
    composable<HomeScreen> {
        HomeTabScreen(
            onNavigateToPetDetail = onNavigateToPetDetail
        )
    }
}