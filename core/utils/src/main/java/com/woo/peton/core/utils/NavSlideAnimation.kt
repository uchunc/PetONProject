package com.woo.peton.core.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

// 1. 들어올 때 (오른쪽 -> 왼쪽)
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInLeft(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
    )
}

// 2. 나갈 때 (왼쪽으로 사라짐)
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutLeft(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
    )
}

// 3. 뒤로가기로 들어올 때 (왼쪽 -> 오른쪽)
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInRight(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
    )
}

// 4. 뒤로가기로 나갈 때 (오른쪽으로 사라짐)
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutRight(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
    )
}