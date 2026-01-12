package com.woo.peton.features.home.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomeSkeletonScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {


        // ⬜ 2. 메인 컨텐츠 (기존 스켈레톤 내용)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            // 슬로건 스켈레톤
            Box(Modifier.width(200.dp).height(24.dp).background(Color.LightGray, RoundedCornerShape(4.dp)))
            Spacer(Modifier.height(24.dp))

            // 펫 카드 스켈레톤
            Box(Modifier.fillMaxWidth().height(120.dp).background(Color.LightGray, RoundedCornerShape(12.dp)))

            Spacer(Modifier.height(32.dp))

            // 배너 스켈레톤 등 추가...
            Box(Modifier.fillMaxWidth().height(80.dp).background(Color.LightGray, RoundedCornerShape(8.dp)))
        }
    }
}