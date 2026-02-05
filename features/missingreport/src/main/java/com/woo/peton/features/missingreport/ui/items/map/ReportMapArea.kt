package com.woo.peton.features.missingreport.ui.items.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.woo.peton.domain.model.ReportPost
import com.woo.peton.features.missingreport.ui.items.map.marker.PetMarker

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun ReportMapArea(
    pets: List<ReportPost>,
    selectedPet: ReportPost?,
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onMarkerClick: (String) -> Unit,
    onMapClick: () -> Unit
) {
    var isMapLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(pets,isMapLoaded) {
        if (isMapLoaded && pets.isNotEmpty() && selectedPet == null) {
            runCatching {
                val firstPet = pets.first()
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(firstPet.latitude, firstPet.longitude), 15f
                    )
                )
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