package com.woo.peton.features.missingreport.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.woo.peton.core.ui.R
import com.woo.peton.core.utils.toFormattedString
import com.woo.peton.core.utils.toRelativeString
import com.woo.peton.domain.model.ReportType
import com.woo.peton.features.missingreport.MissingReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissingReportDetailScreen(
    petId: String,
    viewModel: MissingReportViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pet = remember(uiState.currentPets, petId) {
        uiState.currentPets.find { it.id == petId }
    }

    if (pet == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "상세 정보", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.arrowl),
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* 공유하기 로직 */ }) {
                        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.plus), contentDescription = "공유")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomContactBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color.White)
        ) {
            // 🖼️ 1. 대형 이미지 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.LightGray)
            ) {
                AsyncImage(
                    model = pet.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // 📝 2. 상세 내용 영역
            Column(modifier = Modifier.padding(20.dp)) {

                // 타입 뱃지 & 날짜
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(pet.reportType.colorHex),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = pet.reportType.label,
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = pet.occurrenceDate.toFormattedString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = pet.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = pet.authorName,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "·  ${pet.createdAt.toRelativeString()}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                }

                // 🟢 [수정 1] Divider -> HorizontalDivider 변경
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 24.dp),
                    color = Color(0xFFEEEEEE)
                )

                InfoRow(label = "품종", value = pet.breed)
                InfoRow(label = "발견/실종 장소", value = pet.locationDescription)

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "상세 내용",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = pet.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF424242),
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.5
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "위치 정보",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                DetailMapSection(
                    latitude = pet.latitude,
                    longitude = pet.longitude,
                    type = pet.reportType
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun DetailMapSection(
    latitude: Double,
    longitude: Double,
    type: ReportType
) {
    val location = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 16f)
    }

    val markerState = rememberUpdatedMarkerState(position = location)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                scrollGesturesEnabled = false,
                zoomGesturesEnabled = false,
                rotationGesturesEnabled = false,
                tiltGesturesEnabled = false,
                mapToolbarEnabled = false
            )
        ) {
            // 🟢 [수정 3] MarkerComposable에 remember된 state 전달
            // keys를 주어 재사용성을 높임
            MarkerComposable(
                keys = arrayOf(latitude, longitude),
                state = markerState
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.location),
                    contentDescription = null,
                    tint = Color(type.colorHex),
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

// InfoRow, BottomContactBar는 기존과 동일
@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
fun BottomContactBar() {
    Surface(
        shadowElevation = 16.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { /* 찜하기 */ },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(52.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.book_mark),
                    contentDescription = "관심 등록",
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = { /* 전화 걸기 Intent 등 */ },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6F00)
                )
            ) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.chat), contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "작성자에게 채팅하기", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}