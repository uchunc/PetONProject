package com.woo.peton.ui.component

import com.woo.peton.core.ui.R
import com.woo.peton.core.ui.navigation.ChattingNavigationRoute
import com.woo.peton.core.ui.navigation.HomeNavigationRoute
import com.woo.peton.core.ui.navigation.MissingNavigationRoute
import com.woo.peton.core.ui.navigation.MyPageNavigationRoute
import com.woo.peton.core.ui.navigation.PetOnNavigation

class BottomAppBarItem(
    val tabName: String = "",
    val icon: Int,
    val destination: PetOnNavigation
){
    companion object {
        fun fetchBottomAppBarItems() = listOf(
            BottomAppBarItem(
                tabName = "홈",
                icon = R.drawable.home,
                destination = HomeNavigationRoute.HomeScreen
            ),
            BottomAppBarItem(
                tabName = "실종/제보",
                icon = R.drawable.siren,
                destination = MissingNavigationRoute.MissingScreen(null)
            ),
            BottomAppBarItem(
                tabName = "채팅",
                icon = R.drawable.chat,
                destination = ChattingNavigationRoute.ChattingScreen
            ),
            BottomAppBarItem(
                tabName = "내 정보",
                icon = R.drawable.my_page,
                destination = MyPageNavigationRoute.MyPageScreen
            )
        )
    }
}