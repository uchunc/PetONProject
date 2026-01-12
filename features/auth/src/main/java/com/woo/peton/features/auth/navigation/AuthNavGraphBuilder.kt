package com.woo.peton.features.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.woo.peton.core.ui.navigation.AuthGraph
import com.woo.peton.core.ui.navigation.AuthNavigationRoute
import com.woo.peton.features.auth.ui.screen.AuthTabScreen
import com.woo.peton.features.auth.ui.screen.SignUpTabScreen

fun NavGraphBuilder.authNavBuilder(navController: NavHostController){
    navigation<AuthGraph>(
        startDestination = AuthNavigationRoute.AuthScreen
    ) {
        composable<AuthNavigationRoute.AuthScreen> {
            AuthTabScreen(navController)
        }
        composable<AuthNavigationRoute.SignUpScreen> {
            SignUpTabScreen(navController)
        }
    }
}