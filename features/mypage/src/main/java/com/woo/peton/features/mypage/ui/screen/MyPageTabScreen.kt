package com.woo.peton.features.mypage.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import com.woo.peton.features.mypage.ui.items.FooterSection
import com.woo.peton.features.mypage.ui.items.InfoMenuSection
import com.woo.peton.features.mypage.ui.items.MyProfileCards
import com.woo.peton.features.mypage.ui.items.QuickMenuSection

@Composable
fun MyPageTabScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    onNavigateToPetDetail: (String) -> Unit,
    onNavigateToUserDetail: () -> Unit,
    onNavigateToNotice: () -> Unit,
    onNavigateToChatbot: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

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
                onUserClick = onNavigateToUserDetail,
                onNoticeClick = onNavigateToNotice,
                onChatbotClick = onNavigateToChatbot,
                onLogoutClick = { viewModel.logout() }
            )
        }
    }
}

@Composable
private fun MyPageContent(
    uiState: com.woo.peton.features.mypage.ui.state.MyPageUiState,
    onPetClick: (String) -> Unit,
    onAddPetClick: () -> Unit,
    onUserClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onChatbotClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            MyProfileCards(
                user = uiState.user,
                pets = uiState.pets,
                onPetClick = onPetClick,
                onAddPetClick = onAddPetClick,
                onUserClick = onUserClick
            )
            Spacer(modifier = Modifier.height(30.dp))
        }

        item {
            QuickMenuSection()
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(thickness = 8.dp, color = Color(0xFFEEEEEE))
        }
        item {
            InfoMenuSection(
                onNoticeClick = onNoticeClick,
                onChatbotClick = onChatbotClick,
                onFaqClick = { /* TODO */ },
                onCenterClick = { /* TODO */ }
            )
        }
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