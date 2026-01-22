package com.woo.peton.ui.navhost

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.woo.peton.core.ui.navigation.AuthGraph
import com.woo.peton.core.ui.navigation.MissingNavigationRoute
import com.woo.peton.core.ui.navigation.MyPageNavigationRoute
import com.woo.peton.features.auth.navigation.authNavBuilder
import com.woo.peton.features.chatting.navigation.chatNavBuilder
import com.woo.peton.features.home.navigation.homeNavBuilder
import com.woo.peton.features.missingreport.navigation.missingNavBuilder
import com.woo.peton.features.mypage.navigation.myPageNavBuilder

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AuthGraph,
        modifier = modifier
    ) {
        authNavBuilder(navController)
        homeNavBuilder(
            navController = navController,
            onNavigateToPetDetail = { petId ->
                navController.navigate(MyPageNavigationRoute.MyPetDetailScreen(petId))
            },
            onNavigateToReportPetDetail = { petId ->
                navController.navigate(MissingNavigationRoute.DetailScreen(petId))
            },
            onNavigateToReportList = { reportType ->
                navController.navigate(MissingNavigationRoute.MissingScreen(filterType = reportType.name))
            }
        )
        chatNavBuilder(navController)
        missingNavBuilder(navController)
        myPageNavBuilder(navController)
    }
}