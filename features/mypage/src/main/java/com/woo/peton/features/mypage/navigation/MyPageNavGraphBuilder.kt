package com.woo.peton.features.mypage.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.woo.peton.core.ui.navigation.MyPageNavigationRoute
import com.woo.peton.features.mypage.ui.screen.*

fun NavGraphBuilder.myPageNavBuilder(navController: NavHostController) {

    // 1. 마이페이지 메인
    composable<MyPageNavigationRoute.MyPageScreen> {
        MyPageTabScreen(
            onNavigateToPetDetail = { petId ->
                // Type-Safe Navigation 객체 전달
                navController.navigate(MyPageNavigationRoute.MyPetDetailScreen(petId))
            },
            onNavigateToNotice = {
                // 공지사항 이동 로직
                // navController.navigate(...)
            },
            onNavigateToChatbot = {
                // 챗봇 이동 로직
            }
        )
    }

    // 2. 반려동물 상세/추가 화면
    composable<MyPageNavigationRoute.MyPetDetailScreen> {
        MyPetDetailScreen(navController = navController)
    }
}