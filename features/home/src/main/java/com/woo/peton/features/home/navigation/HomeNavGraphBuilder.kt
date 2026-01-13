package com.woo.peton.features.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.woo.peton.core.ui.navigation.HomeNavigationRoute.HomeScreen
import com.woo.peton.domain.model.ReportType
import com.woo.peton.features.home.ui.screen.HomeTabScreen

fun NavGraphBuilder.homeNavBuilder(
    navController: NavController,
    onNavigateToPetDetail: (String) -> Unit,
    onNavigateToReportPetDetail: (String) -> Unit,
    onNavigateToReportList: (ReportType) -> Unit
) {
    composable<HomeScreen> {
        HomeTabScreen(
            onNavigateToPetDetail = onNavigateToPetDetail,
            onNavigateToReportPetDetail = onNavigateToReportPetDetail,
            onNavigateToReportList = onNavigateToReportList
        )
    }
}