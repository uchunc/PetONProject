package com.woo.peton.features.missingreport.ui.items.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.woo.peton.domain.model.ReportPost
import com.woo.peton.features.missingreport.ui.items.map.marker.PetMarker

@Composable
fun ReportMapArea(
    pets: List<ReportPost>,
    selectedPet: ReportPost?,
    modifier: Modifier = Modifier,
    onMarkerClick: (String) -> Unit,
    onMapClick: () -> Unit
) {
    val defaultSeoul = LatLng(37.5665, 126.9780)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultSeoul, 15f)
    }
    var isMapLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(pets,isMapLoaded) {
        if (isMapLoaded && pets.isNotEmpty() && selectedPet == null) {
            try {
                val boundsBuilder = LatLngBounds.builder()
                pets.forEach { pet ->
                    boundsBuilder.include(LatLng(pet.latitude, pet.longitude))
                }
                val bounds = boundsBuilder.build()

                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngBounds(bounds, 100)
                )
            } catch (e: Exception) {
                e.printStackTrace()
                if (pets.isNotEmpty()) {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(pets.first().latitude, pets.first().longitude), 15f
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(selectedPet) {
        selectedPet?.let { pet ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(pet.latitude, pet.longitude), 16f
                )
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
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
                PetMarker(
                    pet = pet,
                    onClick = { onMarkerClick(pet.id) }
                )
            }
        }

        if (!isMapLoaded) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}