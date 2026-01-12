package com.woo.peton.features.mypage.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woo.peton.core.ui.component.MenuItem

@Composable
fun InfoMenuSection(
    onNoticeClick: () -> Unit = {},
    onChatbotClick: () -> Unit = {},
    onFaqClick: () -> Unit = {},
    onCenterClick: () -> Unit = {}
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
        Text("정보", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        // 각각 별도의 클릭 가능한 행으로 분리
        MenuItem(title = "공지사항", onClick = onNoticeClick)
        MenuItem(title = "1:1 챗봇 문의", onClick = onChatbotClick)
        MenuItem(title = "자주하는 질문", onClick = onFaqClick)
        MenuItem(title = "고객센터", onClick = onCenterClick)
    }
}