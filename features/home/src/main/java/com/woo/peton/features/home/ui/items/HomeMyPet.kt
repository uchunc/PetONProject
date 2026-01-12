package com.woo.peton.features.home.ui.items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.woo.peton.domain.model.MyPet

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyPetSection(
    pets: List<MyPet>,
    onPetClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "🐾 나의 반려 동물",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        )

        if (pets.isEmpty()) {
            // 등록된 펫이 없을 때 표시할 UI
            EmptyPetCard()
        } else {
            // 🟢 [핵심] 여러 마리일 경우 좌우 스크롤 (Pager 사용)
            val pagerState = rememberPagerState(pageCount = { pets.size })

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 20.dp), // 양옆 간격 (다음 카드 살짝 보이기)
                pageSpacing = 12.dp // 카드 사이 간격
            ) { page ->
                MyPetCard(
                    data = pets[page],
                    onClick = { onPetClick(pets[page].id) } // 🟢 클릭 시 ID 전달
                )
            }
        }
    }
}

// 개별 펫 카드 UI
@Composable
private fun MyPetCard(
    data: MyPet,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp)
        ) {
            // 정보 텍스트 영역
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                PetInfoRow("이름", data.name)
                PetInfoRow("성별", data.gender)
                PetInfoRow("종류", data.breed)
                PetInfoRow("나이", data.ageText) // Model의 getter 활용
            }

            // 이미지 영역
            AsyncImage(
                model = data.imageUrl.ifEmpty { null }, // URL이 없으면 placeholder
                contentDescription = "My Pet",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEEEEE)) // 이미지 로딩 전 회색 배경
            )
        }
    }
}

@Composable
private fun EmptyPetCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("등록된 반려동물이 없습니다.\n마이페이지에서 아이를 등록해주세요!", color = Color.Gray)
        }
    }
}

@Composable
private fun PetInfoRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(40.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}