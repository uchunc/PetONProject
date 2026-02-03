package com.woo.peton.features.missingreport.ui.items.map.marker

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.woo.peton.domain.model.ReportPost

@Composable
fun PetMarker(
    pet: ReportPost,
    onClick: () -> Unit
) {
    MarkerComposable(
        keys = arrayOf<Any>(pet.id, pet.reportType),
        state = MarkerState(position = LatLng(pet.latitude, pet.longitude)),
        title = pet.title,
        onClick = {
            onClick()
            true
        }
    ) {
        PetMarkerContent(pet = pet, onClick = onClick)
    }
}