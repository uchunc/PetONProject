package com.woo.peton.features.missingreport.ui.items.map.marker

import android.location.Location
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.woo.peton.core.ui.R

@Composable
fun CurrentLocationMarker(
    location: Location
) {
    val markerState = rememberUpdatedMarkerState(
        position = LatLng(location.latitude, location.longitude)
    )

    val animatedRotation by animateFloatAsState(
        targetValue = location.bearing,
        animationSpec = tween(durationMillis = 1000),
        label = "BearingAnimation"
    )

    val markerSize = 80.dp

    MarkerComposable(
        state = markerState,
        rotation = animatedRotation,
        anchor = Offset(0.5f, 0.5f),
        zIndex = 2f,
    ) {
        Box(
            modifier = Modifier.size(markerSize),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.current_location_marker),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.3f)),
                modifier = Modifier
                    .size(markerSize)
                    .offset(x = 2.dp, y = 2.dp)
            )
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.current_location_marker),
                contentDescription = "Current Location",
                modifier = Modifier.size(markerSize)
            )

        }
    }
}