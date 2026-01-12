package com.woo.peton.features.mypage.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woo.peton.core.ui.R

@Composable
fun QuickMenuSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 아이콘 리소스나 라벨 등은 기획에 따라 변경될 수 있습니다.
        QuickMenuItem(icon = ImageVector.vectorResource(R.drawable.book_mark_full), label = "관심 동물", color = Color(0xFFFF5722))

        VerticalDivider(
            modifier = Modifier
                .height(40.dp)
                .width(1.dp),
            color = Color.LightGray
        )

        QuickMenuItem(icon = ImageVector.vectorResource(R.drawable.notice), label = "실종/제보 기록", color = Color(0xFFFF5722))

        VerticalDivider(
            modifier = Modifier
                .height(40.dp)
                .width(1.dp),
            color = Color.LightGray
        )

        QuickMenuItem(icon = ImageVector.vectorResource(R.drawable.detective), label = "탐정단 의뢰내역", color = Color(0xFFFF5722))
    }
}

@Composable
private fun QuickMenuItem(icon: ImageVector, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}