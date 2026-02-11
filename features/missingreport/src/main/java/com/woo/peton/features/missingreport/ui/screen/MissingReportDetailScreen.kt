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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.woo.peton.core.ui.R
import com.woo.peton.domain.model.ReportPost
import com.woo.peton.features.missingreport.MissingReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissingReportDetailScreen(
    petId: String,
    viewModel: MissingReportViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onEditClick: (ReportPost) -> Unit,
    onDeleteClick: (String) -> Unit,
    onShareClick: () -> Unit,
    onReportClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pet = remember(uiState.pets, petId) {
        uiState.pets.find { it.id == petId }
    }

    val currentUserId by viewModel.currentUserId.collectAsStateWithLifecycle()
    val isAuthor = remember(pet, currentUserId) {
        pet != null && currentUserId != null && pet.authorId == currentUserId
    }

    var isMenuExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    if (pet == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

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
                    Box {
                        IconButton(onClick = { isMenuExpanded = true }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.menu_dots),
                                contentDescription = "옵션"
                            )
                        }
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            if (isAuthor) {
                                DropdownMenuItem(
                                    text = { Text("수정하기") },
                                    onClick = {
                                        isMenuExpanded = false
                                        onEditClick(pet)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("삭제하기", color = Color.Red) },
                                    onClick = {
                                        isMenuExpanded = false
                                        onDeleteClick(pet.id)
                                    }
                                )
                            } else {
                                DropdownMenuItem(
                                    text = { Text("공유하기") },
                                    onClick = {
                                        isMenuExpanded = false
                                        onShareClick()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("신고하기") },
                                    onClick = {
                                        isMenuExpanded = false
                                        onReportClick()
                                    }
                                )
                            }
                        }
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
            ReportPostContent(pet = pet)
        }
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