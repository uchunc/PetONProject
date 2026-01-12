package com.woo.peton.features.missingreport.ui.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.woo.peton.core.ui.R
import com.woo.peton.domain.model.MissingPet

@Composable
fun ReportMapArea(
    pets: List<MissingPet>,
    modifier: Modifier = Modifier,
    onMarkerClick: (String) -> Unit
) {
    // 1. 초기 카메라 위치 (서울 시청)
    val defaultSeoul = LatLng(37.5665, 126.9780)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultSeoul, 15f)
    }

    // 2. 지도가 로드되었는지 확인하는 상태
    var isMapLoaded by remember { mutableStateOf(false) }

    // 3. 데이터가 있고 + 지도가 로드되었을 때만 카메라 이동
    LaunchedEffect(pets, isMapLoaded) {
        if (isMapLoaded && pets.isNotEmpty()) {
            val firstPet = pets.first()
            try {
                cameraPositionState.animate(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(
                            LatLng(firstPet.latitude, firstPet.longitude),
                            15f
                        )
                    )
                )
            } catch (e: Exception) {
                // 초기화 이슈 등으로 실패 시 로그 처리
                e.printStackTrace()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false, // 내 위치 버튼 (권한 필요)
                compassEnabled = false,
                mapToolbarEnabled = false
            ),
            onMapLoaded = {
                // 🟢 지도가 완전히 로드되었음을 알림
                isMapLoaded = true
            },
            onMapClick = {
                // 필요 시 구현
            }
        ) {
            // 마커 생성
            pets.forEach { pet ->
                val petLocation = LatLng(pet.latitude, pet.longitude)

                MarkerComposable(
                    keys = arrayOf(pet.id, pet.reportType),
                    state = MarkerState(position = petLocation),
                    title = pet.title,
                    onClick = {
                        onMarkerClick(pet.id)
                        true
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.location_filled),
                        contentDescription = "${pet.reportType.label} - ${pet.title}",
                        tint = Color(pet.reportType.colorHex),
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
        }

        // 지도가 로딩 중일 때 로딩 인디케이터 표시 (선택사항)
        if (!isMapLoaded) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}