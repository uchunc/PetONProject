package com.woo.peton.features.home.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woo.peton.core.ui.R
import com.woo.peton.domain.model.Banner

// 1. 신고 배너 (고정된 디자인이라 데이터 모델 없이 처리)
@Composable
fun ReportBanner(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFFF6F00), RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable { /* 신고 화면 이동 */ }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("실종 / 보호 신고", color = Color(0xFFFF6F00), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    // 아이콘 리소스 확인 필요 (없으면 arrowr 등으로 대체)
                    Icon(ImageVector.vectorResource(id = R.drawable.siren), contentDescription = null, tint = Color(0xFFFF6F00), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("반려 동물을 잃어버렸거나, 보호 중이신가요?\n펫츠 온에 신고해 빠르게 찾아보세요!", fontSize = 13.sp, color = Color.Black)
            }
            Icon(ImageVector.vectorResource(id = R.drawable.arrowr), contentDescription = null, tint = Color(0xFFFF6F00))
        }
    }
}

// 2. 프로모션 배너 (데이터를 받아서 처리)
@Composable
fun PromoBanner(
    data: Banner,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { data.onClick() },
        colors = CardDefaults.cardColors(containerColor = data.backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = data.description, fontSize = 12.sp, color = Color.Gray)
                Text(text = data.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            data.iconRes?.let {
                Image(painter = painterResource(id = it), contentDescription = null, modifier = Modifier.size(50.dp))
            }
        }
    }
}