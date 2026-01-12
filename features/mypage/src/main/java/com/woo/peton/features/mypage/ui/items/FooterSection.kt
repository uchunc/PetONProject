package com.woo.peton.features.mypage.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woo.peton.core.ui.component.MenuItem

@Composable
fun FooterSection(
    onInviteClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onBizInfoClick: () -> Unit = {}
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        // 친구 초대는 일반 메뉴처럼 생겼으므로 재사용
        MenuItem(title = "친구 초대하기", onClick = onInviteClick)

        Spacer(modifier = Modifier.height(40.dp))

        // 하단 정보 영역
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 로그아웃
            Text(
                text = "로그아웃",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier
                    .clickable(onClick = onLogoutClick)
                    .padding(8.dp) // 터치 영역
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 이용약관 | 개인정보처리방침 (한 줄 배치, 개별 클릭)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                FooterLinkText(text = "이용약관", onClick = onTermsClick)
                FooterDivider()
                FooterLinkText(text = "개인정보처리방침", onClick = onPrivacyClick)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 사업자 정보
            Text(
                text = "(주) 펫온 사업자 정보 ▼",
                color = Color.LightGray,
                fontSize = 10.sp,
                modifier = Modifier
                    .clickable(onClick = onBizInfoClick)
                    .padding(4.dp)
            )
        }
    }
}

// 하단 링크용 작은 텍스트 컴포넌트 (Footer 내부용)
@Composable
private fun FooterLinkText(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = Color.LightGray,
        fontSize = 10.sp,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 4.dp)
    )
}

// 구분선 (|) 컴포넌트
@Composable
private fun FooterDivider() {
    Text(
        text = "|",
        color = Color.LightGray,
        fontSize = 10.sp,
        modifier = Modifier.padding(horizontal = 2.dp)
    )
}