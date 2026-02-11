package com.woo.peton.features.missingreport.ui.screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.woo.peton.core.ui.component.LocalBottomPadding
import com.woo.peton.features.missingreport.MissingReportViewModel
import com.woo.peton.features.missingreport.service.LocationService
import com.woo.peton.features.missingreport.ui.items.bottomsheet.MissingReportBottomSheet
import com.woo.peton.features.missingreport.ui.items.map.CurrentLocationButton
import com.woo.peton.features.missingreport.ui.items.map.ReportMapArea
import com.woo.peton.features.missingreport.ui.items.map.SearchBarAndUtils
import io.morfly.compose.bottomsheet.material3.BottomSheetScaffold
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetScaffoldState
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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
    val currentLocation by viewModel.currentLocation.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val localDensity = LocalDensity.current
    val scope = rememberCoroutineScope()

    //화면 및 레이아웃
    val screenHeight = with(localDensity) {LocalWindowInfo.current.containerSize.height.toDp()}
    val bottomPadding = LocalBottomPadding.current

    val peekHeight = 40.dp
    val collapsedHeight = bottomPadding + peekHeight
    val halfHeight = screenHeight * 0.45f
    val overlapHeight = 28.dp
    val buttonMargin = 16.dp

    var topContentHeight by remember { mutableStateOf(0.dp) }
    var isMyLocationEnabled by remember { mutableStateOf(false) }

    fun startLocationService() {
        val intent = Intent(context, LocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        if (isGranted) {
            isMyLocationEnabled = true
            startLocationService()
        }
    }

    LaunchedEffect(Unit) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val hasPermission = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        if (hasPermission) {
            isMyLocationEnabled = true
            startLocationService()
        } else {
            val permissionsToRequest = permissions.toMutableList()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    val defaultSeoul = LatLng(37.5665, 126.9780)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultSeoul, 15f)
    }

    LaunchedEffect(Unit) {
        val location = snapshotFlow { currentLocation }
            .filterNotNull()
            .first()
        if (uiState.selectedPet == null) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.latitude, location.longitude),
                    15f
                )
            )
        }
    }

    val isSheetActive = uiState.selectedPet != null || viewModel.isFromHome
    val initialDetent = if (isSheetActive) SheetDetent.Half else SheetDetent.Collapsed

    val sheetState = key(topContentHeight){
        rememberBottomSheetState(
            initialValue = initialDetent,
            defineValues = {
                SheetDetent.Collapsed at height(collapsedHeight)
                SheetDetent.Half at height(halfHeight)
                SheetDetent.Expanded at offset(topContentHeight + buttonMargin)
            }
        )
    }

    LaunchedEffect(uiState.selectedPet) {
        if (uiState.selectedPet != null) {
            sheetState.animateTo(SheetDetent.Half)
        }
    }

    LaunchedEffect(sheetState) {
        snapshotFlow { sheetState.currentValue }
            .distinctUntilChanged()
            .filter { it == SheetDetent.Collapsed }
            .collect {
                if (uiState.selectedPet != null) {
                    viewModel.clearSelection()
                }
            }
    }

    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)
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
                    pets = uiState.pets,
                    selectedPet = uiState.selectedPet,
                    onItemClick = { selectedPetId ->
                        viewModel.selectPet(selectedPetId)
                        scope.launch {
                            sheetState.snapTo(SheetDetent.Half)
                        }
                        onNavigateToDetail(selectedPetId)
                    },
                    onBackToList = { viewModel.clearSelection() }
                )
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val visibleHeight = runCatching { sheetState.requireSheetVisibleHeight() }.getOrDefault(0f)
                val halfHeightPx = with(localDensity) { halfHeight.toPx() }
                val overlapHeightPx = with(localDensity) { overlapHeight.toPx() }
                val buttonMarginPx = with(localDensity) { buttonMargin.toPx() }

                val commonShiftOffset = visibleHeight.coerceAtMost(halfHeightPx)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset { IntOffset(x = 0, y = -commonShiftOffset.roundToInt()) }
                ) {
                    ReportMapArea(
                        pets = uiState.pets,
                        selectedPet = uiState.selectedPet,
                        currentLocation = currentLocation,
                        onImageLoaded = viewModel::onImageLoaded,
                        cameraPositionState = cameraPositionState,
                        contentPadding = PaddingValues(bottom = overlapHeight),
                        onMarkerClick = { petId -> viewModel.selectPet(petId) },
                        onMapClick = { viewModel.clearSelection() },
                        onBoundsChanged = { bounds -> viewModel.updateVisibleBounds(bounds) },
                        modifier = Modifier
                            .fillMaxSize()
                            .offset { IntOffset(x = 0, y = overlapHeightPx.roundToInt()) }
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = buttonMargin)
                            .offset { IntOffset(x = 0, y = -buttonMarginPx.roundToInt()) }
                    ) {
                        CurrentLocationButton(
                            onLocationClick = {
                                when {
                                    !isMyLocationEnabled -> {
                                        Toast.makeText(context, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                                        permissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                    }
                                    else -> {
                                        scope.launch {
                                            cameraPositionState.animate(
                                                CameraUpdateFactory.newLatLngZoom(
                                                    LatLng(currentLocation!!.latitude, currentLocation!!.longitude),
                                                    15f
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
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
    }
}