package com.woo.peton.core.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.woo.peton.domain.model.MissingPet
import com.woo.peton.core.utils.toFormattedString

@Composable
fun PetCard(
    pet: MissingPet,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp), // 사진의 둥근 모서리 반영
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 16.dp) // 하단 여백
        ) {
            // 1. 이미지 영역 (꽉 차게)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // 정사각형 비율 (사진에 맞춰 조정 가능, 예: 4f/3f)
                    .background(Color.LightGray)
            ) {
                AsyncImage(
                    model = pet.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // 2. 정보 영역 (배지, 타이틀, 위치, 날짜)
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // --- 배지 & 타이틀 Row ---
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // (1) ReportType 배지 (회색 배경 + 색상 점 + 텍스트)
                    Surface(
                        color = Color(0xFFEEEEEE), // 연한 회색 배경
                        shape = RoundedCornerShape(50), // 캡슐 모양
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            // 상태 색상 점 (Dot)
                            Canvas(modifier = Modifier.size(6.dp)) {
                                drawCircle(color = Color(pet.reportType.colorHex))
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            // 상태 텍스트 (예: 임보, 실종)
                            Text(
                                text = pet.reportType.label,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // (2) 타이틀 (예: 코리안숏헤어)
                    Text(
                        text = pet.title, // 요청하신대로 title 사용 (보통 품종이 들어감)
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // --- 위치 정보 Row ---
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn, // 또는 ImageVector.vectorResource(R.drawable.pin)
                        contentDescription = "위치",
                        tint = Color(0xFFFF5722), // 사진 속 주황색 아이콘 색상
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = pet.locationDescription,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // --- 날짜 정보 Row ---
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange, // 또는 ImageVector.vectorResource(R.drawable.calendar)
                        contentDescription = "날짜",
                        tint = Color(0xFFFF5722), // 사진 속 주황색 아이콘 색상
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = pet.occurrenceDate.toFormattedString(), // 예: 2025.07.10
                        style = MaterialTheme.typography.bodyLarge, // 숫자가 잘 보이게 키움
                        color = Color.Black
                    )
                }
            }
        }
    }
}