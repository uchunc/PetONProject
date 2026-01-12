package com.woo.peton.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavBackStackEntry
import com.woo.peton.core.ui.R

data class TopBarData(
    var title: String = "",
    var titleIcon: ImageVector? = null,
    var actionIcon: ImageVector? = null
)
val NavBackStackEntry.topBarAsRouteName: TopBarData
    @Composable
    get() {
        val routeName = destination.route ?: return TopBarData()
        return when {
            routeName.contains("AuthScreen") -> {
                TopBarData()
            }

            routeName.contains("LocationSelect") -> {
                TopBarData(title = "위치 선택", titleIcon = ImageVector.vectorResource(R.drawable.close))
            }

            routeName.contains("Posting") -> {
                TopBarData(title = "게시물 등록하기", titleIcon = ImageVector.vectorResource(R.drawable.arrowl))
            }

            routeName.contains("Home") || routeName.contains("Missing") || routeName.contains("Chatting")  || routeName.contains("MyPage") -> {
                TopBarData(titleIcon = ImageVector.vectorResource(R.drawable.logo), actionIcon = ImageVector.vectorResource(R.drawable.notification))
            }

            else -> {
                TopBarData(titleIcon = ImageVector.vectorResource(R.drawable.arrowl))
            }
        }
    }