package com.woo.peton.features.home.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.woo.peton.domain.model.ReportType
import com.woo.peton.features.home.HomeViewModel
import com.woo.peton.features.home.ui.items.CustomerCenterSection
import com.woo.peton.features.home.ui.items.DetectiveSection
import com.woo.peton.features.home.ui.items.HomePetListSection
import com.woo.peton.features.home.ui.items.MyPetSection
import com.woo.peton.features.home.ui.items.PromoBanner
import com.woo.peton.features.home.ui.items.ReportBanner
import com.woo.peton.features.home.ui.items.TopSloganSection
import com.woo.peton.features.home.ui.state.HomeUiState

@Composable
fun HomeTabScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPetDetail: (String) -> Unit,
    onNavigateToReportList: (ReportType) -> Unit
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
                    onPetClick = onNavigateToPetDetail,
                    onReportViewAllClick = onNavigateToReportList
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
    onPetClick: (String) -> Unit,
    onReportViewAllClick: (ReportType) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 20.dp)
    ) {
        TopSloganSection(modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(24.dp))

        state.myPet.let { petData ->
            MyPetSection(
                pets = petData,
                onPetClick = onPetClick
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        ReportBanner(modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(32.dp))

        HomePetListSection(
            title = "주인을 찾고 있어요!",
            pets = state.missingPets,
            onPetClick = onPetClick,
            onViewAllClick = { onReportViewAllClick(ReportType.MISSING) }
        )

        HomePetListSection(
            title = "새 가족을 기다리는 임보 동물",
            pets = state.protectionPets,
            onPetClick = onPetClick,
            onViewAllClick = { onReportViewAllClick(ReportType.PROTECTION) }
        )

        HomePetListSection(
            title = "목격된 동물을 알려드려요",
            pets = state.spottedPets,
            onPetClick = onPetClick,
            onViewAllClick = { onReportViewAllClick(ReportType.SPOTTED) }
        )

        state.promoBanner?.let { banner ->
            PromoBanner(data = banner, modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(modifier = Modifier.height(32.dp))
        }

        DetectiveSection(
            items = state.detectives,
            onClick = {},
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))

        CustomerCenterSection()
        Spacer(modifier = Modifier.height(100.dp))
    }
}