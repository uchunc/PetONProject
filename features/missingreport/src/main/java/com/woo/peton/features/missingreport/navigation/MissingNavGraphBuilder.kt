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
        PostingScreen(
            navController = navController,
            onBackClick = {
                navController.popBackStack()
            },
            onLocationSelectClick = {
                navController.navigate(MissingNavigationRoute.LocationSelectScreen)
            }
        )
    }

    composable<MissingNavigationRoute.DetailScreen> { backStackEntry ->
        val route: MissingNavigationRoute.DetailScreen = backStackEntry.toRoute()

        MissingReportDetailScreen(
            petId = route.petId,
            onBackClick = { navController.popBackStack() },
            onEditClick = { post ->
                backStackEntry.savedStateHandle["post_to_edit"] = post
                navController.navigate(MissingNavigationRoute.PostingScreen)
            },
            onDeleteClick = { petId ->
                // 삭제 로직 처리 (예: ViewModel 호출 후 popBackStack 등)
                // 지금은 단순히 뒤로가기 예시
                // navController.popBackStack()
            },
            onShareClick = {/*공유 Intent 실행 로직 등*/ },
            onReportClick = {/*신고 화면 이동 등*/ }
        )
    }

    composable<MissingNavigationRoute.LocationSelectScreen> {
        LocationSelectScreen(navController = navController)
    }
}