package com.woo.peton.features.missingreport.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.woo.peton.core.ui.component.LocalBottomPadding
import com.woo.peton.features.missingreport.MissingReportViewModel
import com.woo.peton.features.missingreport.ui.items.CurrentLocationButton
import com.woo.peton.features.missingreport.ui.items.bottomsheet.MissingReportBottomSheet
import com.woo.peton.features.missingreport.ui.items.map.ReportMapArea
import com.woo.peton.features.missingreport.ui.items.SearchBarAndUtils
import io.morfly.compose.bottomsheet.material3.BottomSheetScaffold
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetScaffoldState
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetState

enum class SheetDetent {
    Collapsed, Half, Expanded
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MissingReportTabScreen(
    viewModel: MissingReportViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit,
    onNavigateToWrite: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val localDensity = LocalDensity.current
    val bottomPadding = LocalBottomPadding.current

    val configuration = LocalConfiguration.current
    val screenHeightPx = with(localDensity) { configuration.screenHeightDp.dp.toPx() }
    val screenHeight = configuration.screenHeightDp.dp

    var topContentHeight by remember { mutableStateOf(0.dp) }

    val peekHeight = 60.dp
    val collapsedHeight = bottomPadding + peekHeight
    val halfHeight = screenHeight * 0.55f
    val buttonMargin = 16.dp

    val sheetState = key(topContentHeight){
        rememberBottomSheetState(
            initialValue = SheetDetent.Collapsed,
            defineValues = {
                SheetDetent.Collapsed at height(collapsedHeight)
                SheetDetent.Half at height(halfHeight)
                SheetDetent.Expanded at offset(topContentHeight + 8.dp)
            }
        )
    }

    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)

    val currentSheetOffsetPx by remember {
        derivedStateOf {
            runCatching { sheetState.requireOffset() }.getOrElse { screenHeightPx }
        }
    }

    val dynamicButtonPadding by remember {
        derivedStateOf {
            val sheetVisibleHeight = screenHeightPx - currentSheetOffsetPx
            with(localDensity) { sheetVisibleHeight.toDp() } + buttonMargin
        }
    }

    val mapContentPadding by remember {
        derivedStateOf {
            val sheetVisibleHeight = screenHeightPx - currentSheetOffsetPx
            PaddingValues(
                top = topContentHeight, // 검색창만큼 위에서 밀기
                bottom = with(localDensity) { sheetVisibleHeight.toDp() } // 시트만큼 아래에서 밀기
            )
        }
    }

    LaunchedEffect(sheetState) {
        if (viewModel.isFromHome) {
            sheetState.animateTo(SheetDetent.Half)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BottomSheetScaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetContainerColor = Color.White,
            sheetTonalElevation = 0.dp,
            sheetShadowElevation = 10.dp,
            sheetContent = {
                MissingReportBottomSheet(
                    pets = uiState.currentPets,
                    selectedPet = uiState.selectedPet,
                    onItemClick = { selectedPetId -> onNavigateToDetail(selectedPetId) },
                    onBackToList = { viewModel.clearSelection() }
                )
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                ReportMapArea(
                    pets = uiState.currentPets,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = mapContentPadding,
                    onMarkerClick = { petId ->
                        viewModel.selectPet(petId)
                    }
                )
            }
        }

        SearchBarAndUtils(
            modifier = Modifier.align(Alignment.TopCenter),
            filters = uiState.filters,
            onFilterToggle = { type -> viewModel.updateFilter(type) },
            onHeightMeasured = { heightPx ->
                topContentHeight = with(localDensity) { heightPx.toDp() }
            },
            onPostingClick = onNavigateToWrite,
            onFavoriteClick = {}
        )

        AnimatedVisibility(
            visible = sheetState.targetValue != SheetDetent.Expanded,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = dynamicButtonPadding)
        ) {
            CurrentLocationButton(
                onLocationClick = {}
            )
        }
    }
}