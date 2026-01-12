package com.woo.peton.core.ui.component

import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage // Coil 라이브러리 사용 가정 (이미지 로드)
import com.woo.peton.domain.model.MissingPet
import com.woo.peton.core.utils.toFormattedString
import com.woo.peton.core.utils.toRelativeString
import com.woo.peton.domain.model.ReportType
import com.woo.peton.core.ui.R

// 1. 홈/리스트용 세로형 카드 (Vertical)
@Composable
fun PetCardVertical(
    pet: MissingPet,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // 사진
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color.LightGray) // 로딩 중 배경
            ) {
                AsyncImage(
                    model = pet.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // 타입 라벨 (좌측 상단)
                Surface(
                    color = Color(pet.reportType.colorHex),
                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = pet.reportType.label,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // 정보 (제목, 종, 날짜)
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = pet.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = pet.breed,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    val dateLabel = when(pet.reportType) {
                        ReportType.MISSING -> "실종"
                        ReportType.SPOTTED -> "목격"
                        ReportType.PROTECTION -> "구조"
                    }
                    Text(
                        text = "$dateLabel ${pet.occurrenceDate.toFormattedString()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Text(
                    text = pet.createdAt.toRelativeString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.LightGray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

// 2. 바텀시트용 가로형 카드 (Horizontal)
@Composable
fun PetCardHorizontal(
    pet: MissingPet,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateLabel = when (pet.reportType) {
        ReportType.MISSING -> "실종일"
        ReportType.SPOTTED -> "목격일"
        ReportType.PROTECTION -> "구조일"
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(120.dp) // 전체 높이 고정
    ) {
        // 1. 왼쪽 큰 사진
        Box(
            modifier = Modifier
                .width(110.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
        ) {
            AsyncImage(
                model = pet.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // 타입 배지
            Surface(
                color = Color(pet.reportType.colorHex),
                modifier = Modifier.align(Alignment.TopStart).padding(4.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = pet.reportType.label,
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 2. 오른쪽 정보 영역
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 상단 정보
            Text(
                text = pet.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${pet.breed} | $dateLabel ${pet.occurrenceDate.toFormattedString()}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 본문 일부 (Snippet)
            Text(
                text = pet.content,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f) // 남은 공간 차지
            )

            // 하단 (댓글 수 & 이동 버튼)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 댓글 수
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.chat),
                        contentDescription = "댓글",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${pet.commentCount}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                }

                // 이동 버튼 (텍스트형 버튼)
                TextButton(
                    onClick = onDetailClick,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.height(24.dp)
                ) {
                    Text("자세히 보기", style = MaterialTheme.typography.labelLarge)
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.arrowr),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}