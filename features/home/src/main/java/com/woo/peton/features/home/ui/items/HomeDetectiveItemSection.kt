package com.woo.peton.features.home.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woo.peton.domain.model.Detective

@Composable
fun DetectiveSection(
    items: List<Detective>,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "전문 탐정단을 추천해 드려요.",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 탐정단 리스트를 가로로 배치 (데이터 개수만큼 균등 분할)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items.forEach { item ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                        .clickable { onClick(item.id) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = item.name, fontSize = 12.sp)
                    // 이미지 사용 시:
                    // Image(painter = painterResource(id = item.imageRes), ...)
                }
            }
        }
    }
}