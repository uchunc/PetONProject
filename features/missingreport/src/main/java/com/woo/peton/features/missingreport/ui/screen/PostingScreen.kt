// features/missingreport/ui/screen/PostingScreen.kt

package com.woo.peton.features.missingreport.ui.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.woo.peton.core.ui.R
import com.woo.peton.core.ui.navigation.MissingNavigationRoute
import com.woo.peton.domain.model.ReportType
import com.woo.peton.features.missingreport.ui.state.PostingUiState
import com.woo.peton.features.missingreport.PostingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostingScreen(
    navController: NavController,
    viewModel: PostingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val title by viewModel.title.collectAsState()
    val breed by viewModel.breed.collectAsState()
    val age by viewModel.age.collectAsState()
    val location by viewModel.locationDescription.collectAsState()
    val content by viewModel.content.collectAsState()
    val selectedType by viewModel.reportType.collectAsState()
    val selectedGender by viewModel.gender.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()

    val currentBackStackEntry = navController.currentBackStackEntry
    val savedStateHandle = currentBackStackEntry?.savedStateHandle

    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.getLiveData<String>("location_name")?.observe(currentBackStackEntry) { name ->
            // 지도에서 받은 데이터가 있으면 처리
            val lat = savedStateHandle.get<Double>("location_lat") ?: 0.0
            val lng = savedStateHandle.get<Double>("location_lng") ?: 0.0

            if (name.isNotEmpty()) {
                viewModel.setLocation(name, lat, lng)

                // 데이터 처리 후 삭제 (재호출 방지)
                savedStateHandle.remove<String>("location_name")
                savedStateHandle.remove<Double>("location_lat")
                savedStateHandle.remove<Double>("location_lng")
            }
        }
    }

    // 사진 선택기 (Photo Picker)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.selectedImageUri.value = uri
        }
    }

    // 결과 처리
    LaunchedEffect(uiState) {
        when (uiState) {
            is PostingUiState.Success -> {
                Toast.makeText(context, "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
                viewModel.resetState()
            }
            is PostingUiState.Error -> {
                Toast.makeText(context, (uiState as PostingUiState.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 0.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    "사진 등록",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable {
                            // 갤러리 실행
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.plus),
                                contentDescription = "Add Photo",
                                tint = Color.Gray,
                                modifier = Modifier.size(40.dp)
                            )
                            Text("사진을 추가해주세요", color = Color.Gray)
                        }
                    }
                }
            }

            // 1. 유형 선택
            item {
                Text("제보 유형", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ReportType.entries.forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { viewModel.reportType.value = type },
                            label = { Text(type.name) }
                        )
                    }
                }
            }

            // 2. 제목
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { viewModel.title.value = it },
                    label = { Text("제목") },
                    placeholder = { Text("예: 강남역 인근 말티즈 찾아요") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // 3. 품종 & 나이
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = breed,
                        onValueChange = { viewModel.breed.value = it },
                        label = { Text("품종") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = age,
                        onValueChange = { viewModel.age.value = it },
                        label = { Text("나이") },
                        placeholder = { Text("예: 5살") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }

            // 4. 성별
            item {
                Text("성별", fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    listOf("수컷", "암컷", "알수없음").forEach { genderOption ->
                        RadioButton(
                            selected = selectedGender == genderOption,
                            onClick = { viewModel.gender.value = genderOption }
                        )
                        Text(genderOption)
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }

            item {
                Text("위치 정보", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = location,
                    onValueChange = {}, // 읽기 전용
                    label = { Text("발견/실종 장소") },
                    placeholder = { Text("터치하여 지도에서 선택하세요") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // 지도 선택 화면으로 이동
                            navController.navigate(MissingNavigationRoute.LocationSelectScreen)
                        },
                    enabled = false, // 키보드 안 올라오게 설정
                    trailingIcon = {
                        Icon(
                            ImageVector.vectorResource(R.drawable.location_filled),
                            contentDescription = "Map"
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            // 6. 상세 내용
            item {
                OutlinedTextField(
                    value = content,
                    onValueChange = { viewModel.content.value = it },
                    label = { Text("상세 내용") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default)
                )
            }

            // 7. 등록 버튼
            item {
                Button(
                    onClick = { viewModel.submitPost() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = uiState !is PostingUiState.Loading && title.isNotEmpty()
                ) {
                    if (uiState is PostingUiState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text("등록하기", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
