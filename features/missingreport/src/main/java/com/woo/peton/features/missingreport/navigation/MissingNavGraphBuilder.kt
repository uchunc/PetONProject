package com.woo.peton.features.missingreport.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.woo.peton.core.ui.navigation.MissingNavigationRoute
import com.woo.peton.features.missingreport.ui.items.post.LocationSelectScreen
import com.woo.peton.features.missingreport.ui.screen.MissingReportDetailScreen
import com.woo.peton.features.missingreport.ui.screen.MissingReportTabScreen
import com.woo.peton.features.missingreport.ui.screen.PostingScreen

fun NavGraphBuilder.missingNavBuilder(navController: NavHostController) {

    composable<MissingNavigationRoute.MissingScreen> {
        MissingReportTabScreen(
            onNavigateToWrite = {
                navController.navigate(MissingNavigationRoute.PostingScreen)
            },
            onNavigateToDetail = { petId ->
                navController.navigate(MissingNavigationRoute.DetailScreen(petId))
            }
        )
    }

    composable<MissingNavigationRoute.PostingScreen> {
        PostingScreen(navController = navController)
    }

    composable<MissingNavigationRoute.DetailScreen> { backStackEntry ->
        val route: MissingNavigationRoute.DetailScreen = backStackEntry.toRoute()

        MissingReportDetailScreen(
            petId = route.petId,
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<MissingNavigationRoute.LocationSelectScreen> {
        LocationSelectScreen(navController = navController)
    }
}