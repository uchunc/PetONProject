package com.woo.peton.features.home.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woo.peton.core.ui.component.MenuItem

@Composable
fun TopSloganSection(modifier: Modifier = Modifier) {
    Text(
        text = buildAnnotatedString {
            append("실종된 반려 동물을\n다 같이 찾는 가장 쉬운 방법, ")
            withStyle(style = SpanStyle(color = Color(0xFFFF6F00), fontWeight = FontWeight.Bold)) {
                append("펫츠 온!")
            }
        },
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp, lineHeight = 30.sp, fontWeight = FontWeight.Bold),
        modifier = modifier.padding(vertical = 20.dp)
    )
}

@Composable
fun ReviewSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("전문 탐정단을 추천해 드려요.", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(modifier = Modifier.weight(1f).height(60.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray), contentAlignment = Alignment.Center) { Text("애니몰스 119", fontSize = 12.sp) }
            Box(modifier = Modifier.weight(1f).height(60.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray), contentAlignment = Alignment.Center) { Text("SOS PET", fontSize = 12.sp) }
        }
    }
}

@Composable
fun CustomerCenterSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        MenuItem(title = "고객센터", onClick = { /* TODO: 고객센터 로직 구현 */ })
        Spacer(modifier = Modifier.height(8.dp))
        Text("평일 09:00 - 18:00 (주말/공휴일 휴무)", color = Color.LightGray, fontSize = 12.sp)
    }
}