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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.woo.peton.domain.model.ReportPost
import com.woo.peton.domain.model.ReportType
import com.woo.peton.features.missingreport.PostingViewModel
import com.woo.peton.features.missingreport.ui.state.PostingUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostingScreen(
    navController: NavController,
    viewModel: PostingViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onLocationSelectClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val title by viewModel.title.collectAsState()
    val animalType by viewModel.animalType.collectAsState()
    val breed by viewModel.breed.collectAsState()
    val age by viewModel.age.collectAsState()
    val location by viewModel.locationDescription.collectAsState()
    val content by viewModel.content.collectAsState()
    val selectedType by viewModel.reportType.collectAsState()
    val selectedGender by viewModel.gender.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()

    var isAnimalTypeExpanded by remember { mutableStateOf(false) }
    val animalTypes = listOf("개", "고양이", "조류", "설치류", "파충류", "기타")

    LaunchedEffect(Unit) {
        val postToEdit = navController.previousBackStackEntry?.savedStateHandle?.get<ReportPost>("post_to_edit")
        if (postToEdit != null) {
            viewModel.initForEdit(postToEdit)
            navController.previousBackStackEntry?.savedStateHandle?.remove<ReportPost>("post_to_edit")
        }
    }

    val currentBackStackEntry = navController.currentBackStackEntry
    val savedStateHandle = currentBackStackEntry?.savedStateHandle

    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.getLiveData<String>("location_name")?.observe(currentBackStackEntry) { name ->
            val lat = savedStateHandle.get<Double>("location_lat") ?: 0.0
            val lng = savedStateHandle.get<Double>("location_lng") ?: 0.0

            if (name.isNotEmpty()) {
                viewModel.setLocation(name, lat, lng)
                savedStateHandle.remove<String>("location_name")
                savedStateHandle.remove<Double>("location_lat")
                savedStateHandle.remove<Double>("location_lng")
            }
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.selectedImageUri.value = uri
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is PostingUiState.Success -> {
                Toast.makeText(context, "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                onBackClick()
                viewModel.resetState()
            }
            is PostingUiState.Error -> {
                Toast.makeText(context, (uiState as PostingUiState.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("게시글 작성", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.arrowl),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text("사진 등록", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray.copy(alpha = 0.3f))
                            .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .clickable {
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

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = isAnimalTypeExpanded,
                            onExpandedChange = { isAnimalTypeExpanded = !isAnimalTypeExpanded },
                            modifier = Modifier.weight(0.35f)
                        ) {
                            OutlinedTextField(
                                value = animalType,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("종류") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAnimalTypeExpanded) },
                                modifier = Modifier.menuAnchor(
                                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                    enabled = true
                                ),
                                singleLine = true
                            )
                            ExposedDropdownMenu(
                                expanded = isAnimalTypeExpanded,
                                onDismissRequest = { isAnimalTypeExpanded = false }
                            ) {
                                animalTypes.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type) },
                                        onClick = {
                                            viewModel.animalType.value = type
                                            isAnimalTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = breed,
                            onValueChange = { viewModel.breed.value = it },
                            label = { Text("품종") },
                            placeholder = { Text("예: 말티즈") },
                            modifier = Modifier.weight(0.65f),
                            singleLine = true
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = age,
                        onValueChange = { viewModel.age.value = it },
                        label = { Text("나이") },
                        placeholder = { Text("예: 5살") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

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
                        onValueChange = {},
                        label = { Text("발견/실종 장소") },
                        placeholder = { Text("터치하여 지도에서 선택하세요") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLocationSelectClick()
                            },
                        enabled = false,
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
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            Button(
                onClick = { viewModel.submitPost() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                enabled = uiState !is PostingUiState.Loading && title.isNotEmpty(),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState is PostingUiState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("등록완료", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
