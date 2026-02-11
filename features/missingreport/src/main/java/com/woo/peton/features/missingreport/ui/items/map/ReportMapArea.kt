package com.woo.peton.features.missingreport.ui.items.map

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.woo.peton.domain.model.ReportPost
import com.woo.peton.features.missingreport.ui.items.map.marker.CurrentLocationMarker
import com.woo.peton.features.missingreport.ui.items.map.marker.PetMarker

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun ReportMapArea(
    pets: List<ReportPost>,
    selectedPet: ReportPost?,
    currentLocation: Location?,
    onImageLoaded: (String) -> Unit,
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onMarkerClick: (String) -> Unit,
    onMapClick: () -> Unit,
    onBoundsChanged: (LatLngBounds) -> Unit
) {
    var isMapLoaded by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    LaunchedEffect(isMapLoaded, selectedPet, pets) {
        if (!isMapLoaded) return@LaunchedEffect

        if (selectedPet != null) {
            val targetLatLng = LatLng(selectedPet.latitude, selectedPet.longitude)
            val yOffsetPx = with(density) { -300.dp.toPx() }

            cameraPositionState.move(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(targetLatLng, 15f)
                )
            )
            cameraPositionState.move(
                CameraUpdateFactory.scrollBy(0f, yOffsetPx)
            )
        }
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            val bounds = cameraPositionState.projection?.visibleRegion?.latLngBounds
            if (bounds != null) {
                onBoundsChanged(bounds)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            contentPadding = contentPadding,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false,
                compassEnabled = false,
                mapToolbarEnabled = false
            ),
            onMapLoaded = { isMapLoaded = true },
            onMapClick = { onMapClick() }
        ) {
            pets.forEach { pet ->
                key(pet.id) {
                    PetMarker(
                        pet = pet,
                        onImageLoaded = { onImageLoaded(pet.id) },
                        onClick = { onMarkerClick(pet.id) }
                    )
                }
            }
            if (currentLocation != null) {
                CurrentLocationMarker(location = currentLocation)
            }
        }

        if (!isMapLoaded) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}