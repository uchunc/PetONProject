package com.woo.peton.features.missingreport.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.woo.peton.core.ui.component.LocalBottomPadding
import com.woo.peton.features.missingreport.MissingReportViewModel
import com.woo.peton.features.missingreport.ui.items.bottomsheet.MissingReportBottomSheet
import com.woo.peton.features.missingreport.ui.items.map.CurrentLocationButton
import com.woo.peton.features.missingreport.ui.items.map.ReportMapArea
import com.woo.peton.features.missingreport.ui.items.map.SearchBarAndUtils
import io.morfly.compose.bottomsheet.material3.BottomSheetScaffold
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetScaffoldState
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetState
import kotlin.math.roundToInt

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
    //val scope = rememberCoroutineScope()

    //화면 및 레이아웃
    val screenHeight = with(localDensity) {LocalWindowInfo.current.containerSize.height.toDp()}
    val bottomPadding = LocalBottomPadding.current

    //높이 설정
    val peekHeight = 40.dp
    val collapsedHeight = bottomPadding + peekHeight
    val halfHeight = screenHeight * 0.45f
    val overlapHeight = 28.dp
    val buttonMargin = 16.dp

    var topContentHeight by remember { mutableStateOf(0.dp) }

    //지도 설정
    val defaultSeoul = LatLng(37.5665, 126.9780)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultSeoul, 15f)
    }

    val sheetState = key(topContentHeight){
        rememberBottomSheetState(
            initialValue = SheetDetent.Collapsed,
            defineValues = {
                SheetDetent.Collapsed at height(collapsedHeight)
                SheetDetent.Half at height(halfHeight)
                SheetDetent.Expanded at offset(topContentHeight + buttonMargin)
            }
        )
    }
    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)

    LaunchedEffect(sheetState) {
        if (viewModel.isFromHome) {
            sheetState.animateTo(SheetDetent.Half)
        }
    }

    LaunchedEffect(uiState.selectedPet) {
        if (uiState.selectedPet != null) {
            sheetState.animateTo(SheetDetent.Half)
        }
    }

    LaunchedEffect(sheetState.targetValue) {
        if (sheetState.targetValue == SheetDetent.Collapsed && uiState.selectedPet != null) {
            viewModel.clearSelection()
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
                    selectedPet = uiState.selectedPet,
                    loadedImageIds = uiState.loadedImageIds,
                    onImageLoaded = viewModel::onImageLoaded,
                    modifier = Modifier
                        .fillMaxSize()
                        .offset {
                            val visibleHeightPx = runCatching {
                                sheetState.requireSheetVisibleHeight()
                            }.getOrDefault(0f)

                            val halfHeightPx = with(localDensity) { halfHeight.toPx() }
                            val overlapHeightPx = with(localDensity) { overlapHeight.toPx() }

                            val targetHeightPx = visibleHeightPx.coerceAtMost(halfHeightPx)

                            val calculatedOffset = (targetHeightPx - overlapHeightPx).coerceAtLeast(0f)

                            IntOffset(x = 0, y = -calculatedOffset.roundToInt())
                        },
                    cameraPositionState = cameraPositionState,
                    contentPadding = PaddingValues(bottom = overlapHeight),
                    onMarkerClick = { petId -> viewModel.selectPet(petId) },
                    onMapClick = { viewModel.clearSelection() }
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
            enter = fadeIn(animationSpec = tween(durationMillis = 150)),
            exit = fadeOut(animationSpec = tween(durationMillis = 150)),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = buttonMargin)
                .offset {
                    val visibleHeight = runCatching {
                        sheetState.requireSheetVisibleHeight()
                    }.getOrDefault(0f)
                    val marginPx = buttonMargin.toPx()

                    IntOffset(x = 0, y = -(visibleHeight + marginPx).roundToInt())
                }
        ) {
            CurrentLocationButton(
                onLocationClick = {
                    /*if (userLocation != null) {
                        scope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newCameraPosition(
                                    CameraPosition.fromLatLngZoom(userLocation!!, 17f)
                                )
                            )
                        }
                    }*/
                }
            )
        }
    }
}