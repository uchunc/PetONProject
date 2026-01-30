package com.woo.peton.features.mypage.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.woo.peton.core.ui.navigation.MyPageNavigationRoute
import com.woo.peton.features.mypage.ui.screen.*

fun NavGraphBuilder.myPageNavBuilder(navController: NavHostController) {
    composable<MyPageNavigationRoute.MyPageScreen> {
        MyPageTabScreen(
            onNavigateToPetDetail = { petId ->
                navController.navigate(MyPageNavigationRoute.MyPetDetailScreen(petId))
            },
            onNavigateToUserDetail = {
                navController.navigate(MyPageNavigationRoute.UserDetailScreen)
            },
            onNavigateToNotice = {
                // 공지사항 이동 로직
            },
            onNavigateToChatbot = {
                // 챗봇 이동 로직
            }
        )
    }

    composable<MyPageNavigationRoute.MyPetDetailScreen> {
        MyPetDetailScreen(navController = navController)
    }

    composable<MyPageNavigationRoute.UserDetailScreen> {
        UserDetailScreen(navController = navController)
    }
}