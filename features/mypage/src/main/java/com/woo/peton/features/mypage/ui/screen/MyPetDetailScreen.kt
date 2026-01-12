package com.woo.peton.features.mypage.ui.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.woo.peton.core.ui.R
import com.woo.peton.features.mypage.MyPetDetailViewModel
import com.woo.peton.features.mypage.ui.state.MyPetDetailUiState
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPetDetailScreen(
    navController: NavController,
    viewModel: MyPetDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }


    LaunchedEffect(uiState.isSaveSuccess) {
        if (uiState.isSaveSuccess) {
            snackBarHostState.showSnackbar("반려동물 정보가 저장되었습니다.")
            // 뒤로가기(popBackStack) 제거 -> 화면 유지됨
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (uiState.isNewPet) "반려동물 등록" else "나의 반려동물")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(ImageVector.vectorResource(R.drawable.arrowl), contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState.isEditing) {
                        // 수정 모드: 저장 버튼
                        TextButton(
                            onClick = { viewModel.savePet() },
                            enabled = !uiState.isSaving
                        ) {
                            Text("완료", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    } else {
                        // 조회 모드: 수정 버튼
                        IconButton(onClick = { viewModel.startEditing() }) {
                            Icon(ImageVector.vectorResource(R.drawable.edit), contentDescription = "Edit")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            MyPetDetailContent(
                modifier = Modifier.padding(paddingValues),
                uiState = uiState,
                // ViewModel 함수 연결
                onNameChange = viewModel::onNameChange,
                onBreedChange = viewModel::onBreedChange,
                onGenderChange = viewModel::onGenderChange,
                onBirthDateChange = viewModel::onBirthDateChange,
                onNeuteredChange = viewModel::onNeuteredChange,
                onRegistrationNumberChange = viewModel::onRegistrationNumberChange,
                onContentChange = viewModel::onContentChange,
                onImageChange = viewModel::onImageChange
            )
        }
    }
}

@Composable
private fun MyPetDetailContent(
    modifier: Modifier = Modifier,
    uiState: MyPetDetailUiState,
    onNameChange: (String) -> Unit,
    onBreedChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onNeuteredChange: (Boolean) -> Unit,
    onRegistrationNumberChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onImageChange: (String) -> Unit
) {
    val isEditing = uiState.isEditing
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onImageChange(uri.toString())
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // 1. 프로필 이미지
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable(enabled = isEditing) {
                    // 🟢 [수정] 포토피커 실행
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            if (uiState.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = uiState.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.plus),
                        contentDescription = null,
                        tint = Color.White
                    )
                    if (isEditing) {
                        Text("사진 등록", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))

        // 2. 기본 정보
        PetInputRow(
            label = "이름",
            value = uiState.name,
            onValueChange = onNameChange,
            isEditing = isEditing,
            required = true
        )

        PetInputRow(
            label = "품종",
            value = uiState.breed,
            onValueChange = onBreedChange,
            isEditing = isEditing,
            required = true
        )

        // 성별
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("성별 *", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
            if (isEditing) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    listOf("남", "여").forEach { g ->
                        SelectableButton(
                            text = g,
                            isSelected = uiState.gender == g,
                            onClick = { onGenderChange(g) }
                        )
                    }
                }
            } else {
                Text(uiState.gender, color = Color.Black)
            }
        }
        HorizontalDivider(color = Color(0xFFEEEEEE))

        // 생일 & 나이
        BirthdayInputRow(
            date = uiState.birthDate,
            ageText = uiState.ageText,
            isEditing = isEditing,
            onDateSelected = onBirthDateChange
        )
        HorizontalDivider(color = Color(0xFFEEEEEE))

        // 중성화 여부
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("중성화 *", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
            if (isEditing) {
                IconToggleButton(
                    checked = uiState.neutered,
                    onCheckedChange = onNeuteredChange
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.check),
                        contentDescription = null,
                        tint = if (uiState.neutered) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            } else {
                Text(if (uiState.neutered) "했음" else "안 했음")
            }
        }
        HorizontalDivider(color = Color(0xFFEEEEEE))

        // 동물등록번호
        PetInputRow(
            label = "동물등록번호",
            value = uiState.registrationNumber,
            onValueChange = onRegistrationNumberChange,
            isEditing = isEditing,
            required = false,
            placeholder = "선택사항"
        )

        // 특징 (내용)
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
            Text("특징 및 성격", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            if (isEditing) {
                OutlinedTextField(
                    value = uiState.content,
                    onValueChange = onContentChange,
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    placeholder = { Text("아이의 성격이나 좋아하는 것 등을 적어주세요.") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            } else {
                Text(
                    text = uiState.content.ifEmpty { "등록된 특징이 없습니다." },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8F9FA), RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    color = if (uiState.content.isEmpty()) Color.Gray else Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}

// --- 공통 컴포넌트 ---

@Composable
fun PetInputRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditing: Boolean,
    required: Boolean,
    placeholder: String = ""
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (required) "$label *" else label,
                modifier = Modifier.width(80.dp),
                fontWeight = FontWeight.Bold
            )
            if (isEditing) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(placeholder) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            } else {
                Text(
                    text = value.ifEmpty { "-" },
                    modifier = Modifier.weight(1f).padding(vertical = 12.dp),
                    color = Color.Black
                )
            }
        }
        HorizontalDivider(color = Color(0xFFEEEEEE))
    }
}

@Composable
fun SelectableButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFFFF5722) else Color.White
    val textColor = if (isSelected) Color.White else Color.Gray
    val borderColor = if (isSelected) Color(0xFFFF5722) else Color.LightGray

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(text, color = textColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BirthdayInputRow(
    date: String,
    ageText: String,
    isEditing: Boolean,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val formattedDate = String.format(Locale.US,"%04d-%02d-%02d", year, month + 1, dayOfMonth)
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("생일", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)

        Box(
            modifier = Modifier
                .weight(1f)
                .clickable(enabled = isEditing) { datePickerDialog.show() }
                .border(
                    width = if (isEditing) 1.dp else 0.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = date.ifEmpty { "YYYY.MM.DD" },
                    color = if (date.isEmpty()) Color.Gray else Color.Black
                )
                if (isEditing) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.calendar),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))
        Text("나이", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .border(1.dp, Color.LightGray, RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(text = ageText.ifEmpty { "0살" }, fontSize = 12.sp)
        }
    }
}