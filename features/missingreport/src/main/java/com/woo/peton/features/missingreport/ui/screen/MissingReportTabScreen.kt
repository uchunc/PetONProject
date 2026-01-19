package com.woo.peton.features.missingreport.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.woo.peton.features.missingreport.MissingReportViewModel
import com.woo.peton.features.missingreport.ui.items.ActionButtons
import com.woo.peton.features.missingreport.ui.items.MissingReportBottomSheet
import com.woo.peton.features.missingreport.ui.items.ReportMapArea
import com.woo.peton.features.missingreport.ui.items.SearchBarAndFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissingReportTabScreen(
    viewModel: MissingReportViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit,
    onNavigateToWrite: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedPet by viewModel.selectedPet.collectAsStateWithLifecycle()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )

    val localDensity = LocalDensity.current
    var topContentHeight by remember { mutableStateOf(0.dp) }

    val isSheetExpanded = scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded

    LaunchedEffect(Unit) {
        if (viewModel.isFromHome) {
            scaffoldState.bottomSheetState.expand()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BottomSheetScaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            sheetPeekHeight = 140.dp,

            sheetContainerColor = Color.Transparent,
            sheetShadowElevation = 0.dp,
            sheetDragHandle = null,
            sheetShape = RectangleShape,
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = topContentHeight + 8.dp)
                ) {
                    MissingReportBottomSheet(
                        pets = uiState.currentPets,
                        selectedPet = selectedPet,
                        onItemClick = { selectedPetId ->
                            onNavigateToDetail(selectedPetId)
                        },
                        onBackToList = {
                            viewModel.clearSelection()
                        }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
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
            showLocationButton = !isSheetExpanded,
            onPostingClick = onNavigateToWrite,
            onFavoriteClick = {},
            onLocationClick = {}
        )
    }
}