package com.woo.peton.features.missingreport.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.woo.peton.features.missingreport.MissingReportViewModel
import com.woo.peton.features.missingreport.ui.items.ActionButtons
import com.woo.peton.features.missingreport.ui.items.MissingReportBottomSheet
import com.woo.peton.features.missingreport.ui.items.ReportMapArea
import com.woo.peton.features.missingreport.ui.items.SearchBarAndFilter
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
    var topContentHeight by remember { mutableStateOf(0.dp) }

    val sheetState = rememberBottomSheetState(
        initialValue = SheetDetent.Collapsed,
        defineValues = {
            SheetDetent.Collapsed at height(140.dp)
            SheetDetent.Half at height(percent = 55)
            if (topContentHeight > 0.dp) {
                SheetDetent.Expanded at offset(dp = topContentHeight + 48.dp)
            } else {
                SheetDetent.Expanded at height(percent = 90)
            }
        }
    )

    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)

    LaunchedEffect(Unit) {
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
                    onMarkerClick = { petId ->
                        viewModel.selectPet(petId)
                    }
                )
            }
        }

        SearchBarAndFilter(
            modifier = Modifier.align(Alignment.TopCenter),
            filters = uiState.filters,
            onFilterToggle = { type -> viewModel.updateFilter(type) },
            onHeightMeasured = { heightPx ->
                topContentHeight = with(localDensity) { heightPx.toDp() }
            }
        )

        ActionButtons(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 156.dp),
            onPostingClick = onNavigateToWrite,
            onFavoriteClick = {},
            onLocationClick = {}
        )
    }
}