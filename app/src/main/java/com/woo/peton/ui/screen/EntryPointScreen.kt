package com.woo.peton.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.woo.peton.core.ui.navigation.*
import com.woo.peton.ui.MainViewModel
import com.woo.peton.ui.component.*
import com.woo.peton.ui.navhost.AppNavHost

private val MainTabRoutes = setOf(
    HomeNavigationRoute.HomeScreen::class.qualifiedName,
    MissingNavigationRoute.MissingScreen::class.qualifiedName,
    ChattingNavigationRoute.ChattingScreen::class.qualifiedName,
    MyPageNavigationRoute.MyPageScreen::class.qualifiedName
)
private val FullScreenRoutes = setOf(
    MyPageNavigationRoute.MyPetDetailScreen::class.qualifiedName,
    AuthNavigationRoute.AuthScreen::class.qualifiedName
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPointScreen(
    viewModel: MainViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val bottomAppBarItems = remember { BottomAppBarItem.fetchBottomAppBarItems() }

    val currentRoute = navBackStackEntry?.destination?.route.orEmpty()
    val topBarData = navBackStackEntry?.topBarAsRouteName ?: TopBarData()

    val isShowBars by remember(currentRoute) {
        derivedStateOf {
            // 1. 현재 라우트가 메인 탭에 포함되는지 확인
            val isMainTab = MainTabRoutes.any { route ->
                route != null && currentRoute.contains(route)
            }
            // 2. 현재 라우트가 전체 화면(숨김) 목록에 포함되는지 확인
            val isFullScreen = FullScreenRoutes.any { route ->
                route != null && currentRoute.contains(route)
            }

            // 최종 결정: 메인 탭이어야 하고, 전체 화면 목록이 아니어야 하며, 홈 로딩 중이 아니어야 함
            isMainTab && !isFullScreen
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isShowBars) {
                MainTopBar(
                    topBarData = topBarData,
                    onBackClick = { navController.popBackStack() },
                    onActionClick = { /* TODO: 알림 화면 이동 */ }
                )
            }
        },
        bottomBar = {
            if (isShowBars) {
                MainBottomBar(
                    items = bottomAppBarItems,
                    currentRoute = currentRoute,
                    onNavigate = { destination ->
                        navController.navigate(destination) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
        )
    }
}