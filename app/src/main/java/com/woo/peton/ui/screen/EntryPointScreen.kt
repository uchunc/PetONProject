package com.woo.peton.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.woo.peton.core.ui.component.LocalBottomPadding
import com.woo.peton.core.ui.navigation.AuthNavigationRoute
import com.woo.peton.core.ui.navigation.ChattingNavigationRoute
import com.woo.peton.core.ui.navigation.HomeNavigationRoute
import com.woo.peton.core.ui.navigation.MissingNavigationRoute
import com.woo.peton.core.ui.navigation.MyPageNavigationRoute
import com.woo.peton.ui.MainViewModel
import com.woo.peton.ui.component.BottomAppBarItem
import com.woo.peton.ui.component.MainBottomBar
import com.woo.peton.ui.component.MainTopBar
import com.woo.peton.ui.component.TopBarData
import com.woo.peton.ui.component.topBarAsRouteName
import com.woo.peton.ui.navhost.AppNavHost

private val MainTabRoutes = setOf(
    HomeNavigationRoute.HomeScreen::class.qualifiedName,
    MissingNavigationRoute.MissingScreen::class.qualifiedName,
    ChattingNavigationRoute.ChattingScreen::class.qualifiedName,
    MyPageNavigationRoute.MyPageScreen::class.qualifiedName
)
private val FullScreenRoutes = setOf(
    MyPageNavigationRoute.MyPetDetailScreen::class.qualifiedName,
    AuthNavigationRoute.AuthScreen::class.qualifiedName,
    MissingNavigationRoute.PostingScreen::class.qualifiedName,
    MissingNavigationRoute.DetailScreen::class.qualifiedName
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
            val isMainTab = MainTabRoutes.any { route ->
                route != null && currentRoute.contains(route)
            }
            val isFullScreen = FullScreenRoutes.any { route ->
                route != null && currentRoute.contains(route)
            }
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
        CompositionLocalProvider(
            LocalBottomPadding provides paddingValues.calculateBottomPadding()
        ){
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(
                    top = if (isShowBars) paddingValues.calculateTopPadding() else 0.dp
                )
            )
        }
    }
}