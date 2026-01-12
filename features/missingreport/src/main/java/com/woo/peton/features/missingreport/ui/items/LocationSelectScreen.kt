package com.woo.peton.features.missingreport.ui.items

import android.Manifest
import android.os.Build
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlinx.coroutines.withContext
import java.util.Locale
import com.woo.peton.core.ui.R

@Composable
fun LocationSelectScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // 1. 초기 카메라 위치 (서울 시청)
    val defaultSeoul = LatLng(37.5665, 126.9780)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultSeoul, 15f)
    }
    // 현재 화면 중앙 좌표 및 주소
    var centerLocation by remember { mutableStateOf(defaultSeoul) }
    var addressText by remember { mutableStateOf("위치 확인 중...") }
    // 내 위치 버튼 활성화를 위한 권한 확인
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            hasLocationPermission = true
            // 권한 허용 시 현재 위치로 이동
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        scope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(location.latitude, location.longitude), 17f
                                )
                            )
                        }
                    }
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    // 진입 시 권한 요청
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        } else {
            // 이미 권한 있으면 현재 위치로 이동
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        scope.launch {
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                                LatLng(location.latitude, location.longitude), 17f
                            )
                        }
                    }
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    // 카메라 이동이 멈추면 중앙 좌표 갱신 및 주소 변환
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            centerLocation = cameraPositionState.position.target
            scope.launch {
                addressText = getAddressFromLatLng(context, centerLocation)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 1. 구글 맵
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = true,
                zoomControlsEnabled = false,
                compassEnabled = false
            )
        )

        // 2. 중앙 고정 핀
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.location_filled),
            contentDescription = "Center Pin",
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.Center)
                .offset(y = (-24).dp),
            tint = MaterialTheme.colorScheme.error
        )

        // 3. 상단 주소 표시 카드
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                // 상단바 바로 아래에 붙도록 top 패딩을 적절히 줍니다.
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(ImageVector.vectorResource(R.drawable.location), contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = addressText, style = MaterialTheme.typography.bodyMedium)
            }
        }

        LargeFloatingActionButton(
            onClick = {
                navController.previousBackStackEntry?.savedStateHandle?.apply {
                    set("location_name", addressText)
                    set("location_lat", centerLocation.latitude)
                    set("location_lng", centerLocation.longitude)
                }
                navController.popBackStack()
            },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 100.dp, end = 16.dp)
        ) {
            Icon(
                ImageVector.vectorResource(R.drawable.check),
                contentDescription = "Select",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

// 주소 변환 함수 (이전과 동일)
suspend fun getAddressFromLatLng(context: Context, latLng: LatLng): String {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.KOREA)

            val addresses = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // 신규 API (비동기 -> 코루틴으로 변환)
                suspendCancellableCoroutine { continuation ->
                    geocoder.getFromLocation(
                        latLng.latitude,
                        latLng.longitude,
                        1
                    ) { result ->
                        // 콜백 결과를 코루틴으로 반환
                        continuation.resume(result)
                    }
                }
            } else {
                // 기존 API (동기 방식) - 경고 무시 처리
                @Suppress("DEPRECATION")
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            }

            if (!addresses.isNullOrEmpty()) {
                // "대한민국 " 같은 불필요한 국가명 제거
                addresses[0].getAddressLine(0).replace("대한민국 ", "")
            } else {
                "위치 정보를 찾을 수 없음"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "주소 불러오기 실패"
        }
    }
}