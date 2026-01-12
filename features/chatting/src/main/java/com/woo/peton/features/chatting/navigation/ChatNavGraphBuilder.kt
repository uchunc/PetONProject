package com.woo.peton.features.chatting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.woo.peton.core.ui.navigation.ChattingNavigationRoute
import com.woo.peton.features.chatting.ui.screen.ChattingTabScreen

fun NavGraphBuilder.chatNavBuilder(navController: NavHostController){
    composable<ChattingNavigationRoute.ChattingScreen> {
        ChattingTabScreen()
    }
}