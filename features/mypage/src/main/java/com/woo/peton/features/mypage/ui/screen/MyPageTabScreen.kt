package com.woo.peton.features.mypage.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.woo.peton.features.mypage.MyPageViewModel
import com.woo.peton.features.mypage.ui.items.*

@Composable
fun MyPageTabScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    onNavigateToPetDetail: (String) -> Unit,
    onNavigateToNotice: () -> Unit,
    onNavigateToChatbot: () -> Unit,
) {
    // 3. 상태 수집 (Lifecycle 인지)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    // 🟢 [추가] 화면이 다시 보일 때(ON_RESUME) 데이터 새로고침
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadMyPageData()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // 화면 그리기
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            MyPageContent(
                uiState = uiState,
                onPetClick = { petId -> onNavigateToPetDetail(petId) },
                onAddPetClick = { onNavigateToPetDetail("new") },
                onNoticeClick = onNavigateToNotice,
                onChatbotClick = onNavigateToChatbot,
                onLogoutClick = { viewModel.logout() }
            )
        }
    }
}

// UI 구성 요소 배치 (State Hoisting)
@Composable
private fun MyPageContent(
    uiState: com.woo.peton.features.mypage.ui.state.MyPageUiState,
    onPetClick: (String) -> Unit,
    onAddPetClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onChatbotClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // 1. 프로필 카드 영역
        item {
            Spacer(modifier = Modifier.height(20.dp))
            MyProfileCards(
                user = uiState.user,
                pets = uiState.pets,
                onPetClick = onPetClick,
                onAddPetClick = onAddPetClick
            )
            Spacer(modifier = Modifier.height(30.dp))
        }

        // 2. 퀵 메뉴 영역
        item {
            QuickMenuSection()
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(thickness = 8.dp, color = Color(0xFFEEEEEE))
        }

        // 3. 정보 메뉴 영역
        item {
            InfoMenuSection(
                onNoticeClick = onNoticeClick,
                onChatbotClick = onChatbotClick,
                onFaqClick = { /* TODO */ },
                onCenterClick = { /* TODO */ }
            )
        }

        // 4. 하단 영역 (로그아웃 등)
        item {
            FooterSection(
                onInviteClick = { /* TODO */ },
                onLogoutClick = onLogoutClick,
                onTermsClick = { /* TODO */ },
                onPrivacyClick = { /* TODO */ },
                onBizInfoClick = { /* TODO */ }
            )
        }
    }
}