package com.woo.peton.features.mypage.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.woo.peton.core.ui.R
import com.woo.peton.features.mypage.UserDetailViewModel
import com.woo.peton.features.mypage.ui.state.UserDetailUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    navController: NavController,
    viewModel: UserDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSaveSuccess) {
        if (uiState.isSaveSuccess) {
            snackBarHostState.showSnackbar("프로필이 저장되었습니다.")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("내 정보 수정") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(ImageVector.vectorResource(R.drawable.arrowl), contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState.isEditing) {
                        TextButton(
                            onClick = { viewModel.saveProfile() },
                            enabled = !uiState.isSaving
                        ) {
                            Text("완료", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    } else {
                        IconButton(onClick = { viewModel.startEditing() }) {
                            Icon(ImageVector.vectorResource(R.drawable.edit), contentDescription = "Edit")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            UserDetailContent(
                modifier = Modifier.padding(padding),
                uiState = uiState,
                onNameChange = viewModel::onNameChange,
                onPhoneChange = viewModel::onPhoneChange,
                onAddressChange = viewModel::onAddressChange,
                onImageChange = viewModel::onImageChange
            )
        }
    }
}

@Composable
private fun UserDetailContent(
    modifier: Modifier,
    uiState: UserDetailUiState,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
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
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable(enabled = isEditing) {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            if (uiState.profileImageUrl.isNotEmpty()) {
                AsyncImage(
                    model = uiState.profileImageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.my_page),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
            if (isEditing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.plus),
                        contentDescription = "Change Photo",
                        tint = Color.White
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))

        UserInfoRow("이름", uiState.name, onNameChange, isEditing)
        UserInfoRow("연락처", uiState.phoneNumber, onPhoneChange, isEditing)
        UserInfoRow("주소", uiState.address, onAddressChange, isEditing)

        UserInfoRow("이메일", uiState.email, {}, isEditing = false, enabled = false)
    }
}

@Composable
fun UserInfoRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditing: Boolean,
    enabled: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
        if (isEditing && enabled) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray
                )
            )
        } else {
            Text(
                text = value.ifEmpty { "-" },
                modifier = Modifier.padding(vertical = 12.dp),
                fontSize = 16.sp,
                color = if (enabled) Color.Black else Color.Gray
            )
            HorizontalDivider(color = Color(0xFFEEEEEE))
        }
    }
}