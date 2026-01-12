package com.woo.peton.features.home.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.woo.peton.core.ui.component.PetCardVertical
import com.woo.peton.core.ui.component.SectionHeader
import com.woo.peton.features.home.HomeViewModel
import com.woo.peton.features.home.ui.items.*
import com.woo.peton.features.home.ui.state.HomeUiState

@Composable
fun HomeTabScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPetDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                HomeSkeletonScreen()
            }
            is HomeUiState.Success -> {
                HomeContent(
                    state = state,
                    onPetClick = onNavigateToPetDetail
                )
            }
            is HomeUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "오류: ${state.message}")
                }
            }
        }
    }

}

@Composable
private fun HomeContent(
    state: HomeUiState.Success,
    onPetClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 20.dp)
    ) {
        // 1. 슬로건
        TopSloganSection(modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(24.dp))

        // 2. 나의 반려동물
        state.myPet.let { petData ->
            MyPetSection(
                pets = petData,
                onPetClick = onPetClick
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 3. 신고 배너
        ReportBanner(modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(32.dp))

        // 4. 실종 동물 리스트 (LazyRow -> PetCardVertical 사용)
        if (state.missingPets.isNotEmpty()) {
            Column {
                SectionHeader(
                    title = "주인을 찾고 있어요!",
                    onMoreClick = {},
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // [변경] MissingPet 객체를 그대로 넘김
                    items(state.missingPets) { pet ->
                        PetCardVertical(
                            pet = pet,
                            onClick = { /* 상세 페이지 이동 로직 */ },
                            modifier = Modifier.width(160.dp) // 카드의 너비 지정
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // 5. 임보 동물 리스트 (LazyRow -> PetCardVertical 사용)
        if (state.fosterPets.isNotEmpty()) {
            Column {
                SectionHeader(
                    title = "새 가족을 기다리는 임보 동물",
                    onMoreClick = {},
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.fosterPets) { pet ->
                        PetCardVertical(
                            pet = pet,
                            onClick = { /* 상세 페이지 이동 로직 */ },
                            modifier = Modifier.width(160.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // 6. 프로모션 배너
        state.promoBanner?.let { banner ->
            PromoBanner(data = banner, modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(modifier = Modifier.height(32.dp))
        }

        // 7. 탐정 섹션
        DetectiveSection(
            items = state.detectives,
            onClick = {},
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))

        // 8. 고객센터
        CustomerCenterSection()
        Spacer(modifier = Modifier.height(100.dp))
    }
}