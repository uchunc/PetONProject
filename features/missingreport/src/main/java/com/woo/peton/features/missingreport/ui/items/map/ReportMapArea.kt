package com.woo.peton.features.missingreport.ui.items.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.woo.peton.core.ui.R
import com.woo.peton.domain.model.ReportPost

@Composable
fun ReportMapArea(
    pets: List<ReportPost>,
    modifier: Modifier = Modifier,
    onMarkerClick: (String) -> Unit
) {
    val defaultSeoul = LatLng(37.5665, 126.9780)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultSeoul, 15f)
    }

    var isMapLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(pets, isMapLoaded) {
        if (isMapLoaded && pets.isNotEmpty()) {
            val firstPet = pets.first()
            try {
                cameraPositionState.animate(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(
                            LatLng(firstPet.latitude, firstPet.longitude),
                            15f
                        )
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
            onMapLoaded = {
                isMapLoaded = true
            },
            onMapClick = {
            }
        ) {
            pets.forEach { pet ->
                val petLocation = LatLng(pet.latitude, pet.longitude)

                MarkerComposable(
                    keys = arrayOf<Any>(pet.id, pet.reportType),
                    state = MarkerState(position = petLocation),
                    title = pet.title,
                    onClick = {
                        onMarkerClick(pet.id)
                        true
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.location_filled),
                        contentDescription = "${pet.reportType.label} - ${pet.title}",
                        tint = Color(pet.reportType.colorHex),
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
        }

        if (!isMapLoaded) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}