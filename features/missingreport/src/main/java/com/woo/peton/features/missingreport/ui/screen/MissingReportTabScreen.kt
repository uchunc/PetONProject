package com.woo.peton.features.missingreport.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.woo.peton.features.missingreport.MissingReportViewModel
import com.woo.peton.features.missingreport.ui.items.ActionButtons
import com.woo.peton.features.missingreport.ui.items.ReportMapArea
import com.woo.peton.features.missingreport.ui.items.MissingReportBottomSheet
import com.woo.peton.features.missingreport.ui.items.SearchBarAndFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissingReportTabScreen(
    viewModel: MissingReportViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit,
    onNavigateToWrite: () -> Unit
) {
    // 1. 상태 수집
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedPet by viewModel.selectedPet.collectAsStateWithLifecycle()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )

    // 화면 크기 및 높이 계산
    val peekHeight = 140.dp
    val localDensity = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    var topContentHeight by remember { mutableStateOf(0.dp) }
    val sheetMaxHeight = screenHeight - topContentHeight
    val isSheetExpanded = scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded

    Box(modifier = Modifier.fillMaxSize()) {

        // [Layer 1] 지도 및 서랍
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = peekHeight,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetContainerColor = Color.White,
            sheetShadowElevation = 10.dp,
            sheetDragHandle = null,
            sheetContent = {
                // 🟢 [수정] 바텀시트에 상태와 콜백 전달
                MissingReportBottomSheet(
                    height = sheetMaxHeight,
                    pets = uiState.currentPets,
                    selectedPet = selectedPet, // 선택된 펫 정보 전달
                    onItemClick = { petId ->
                        // 리스트 아이템 클릭 -> 펫 선택
                        viewModel.selectPet(petId)
                    },
                    onDetailClick = {
                        // 요약 카드 클릭 -> 전체 상세 화면 이동
                        if (selectedPet != null) {
                            onNavigateToDetail(selectedPet!!.id)
                        }
                    },
                    onBackToList = {
                        // 요약 화면에서 뒤로가기 -> 선택 해제
                        viewModel.clearSelection()
                    }
                )
            }
        ) {
            // 지도 영역
            Box(modifier = Modifier.fillMaxSize()) {
                ReportMapArea(
                    pets = uiState.currentPets,
                    onMarkerClick = { petId ->
                        // 🟢 [구현] 마커 클릭 시 뷰모델에 선택 요청
                        viewModel.selectPet(petId)
                    }
                )
            }
        }

        // [Layer 2] 상단 검색바 & 필터
        SearchBarAndFilter(
            modifier = Modifier.align(Alignment.TopCenter),
            filters = uiState.filters,
            onFilterToggle = { type -> viewModel.toggleFilter(type) },
            onHeightMeasured = { heightPx -> with(localDensity) { heightPx.toDp() } }
        )

        // [Layer 3] 우측 하단 버튼들
        ActionButtons(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = peekHeight + 16.dp),
            showLocationButton = !isSheetExpanded,
            onPostingClick = onNavigateToWrite,
            onFavoriteClick = {},
            onLocationClick = {}
        )
    }
}