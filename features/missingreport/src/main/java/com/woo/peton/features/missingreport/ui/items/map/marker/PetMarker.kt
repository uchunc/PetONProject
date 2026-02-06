package com.woo.peton.features.missingreport.ui.items.map.marker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.woo.peton.core.ui.R
import com.woo.peton.domain.model.ReportPost
import kotlinx.coroutines.flow.filter

@Composable
fun PetMarker(
    pet: ReportPost,
    isImageLoaded: Boolean,
    onImageLoaded: () -> Unit,
    onClick: () -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(pet.imageUrl.takeIf { it.isNotEmpty() })
            .size(with(LocalDensity.current) { 36.dp.roundToPx() })
            .scale(Scale.FIT)
            .allowHardware(false)
            .crossfade(false)
            .build()
    )

    LaunchedEffect(painter) {
        snapshotFlow { painter.state }
            .filter { it is AsyncImagePainter.State.Success }
            .collect {
                withFrameNanos { }
                onImageLoaded()
            }
    }

    val markerState = remember(pet.id) {
        MarkerState(position = LatLng(pet.latitude, pet.longitude))
    }.apply{
        position = LatLng(pet.latitude, pet.longitude)
    }

    MarkerComposable(
        keys = arrayOf<Any>(pet.id, pet.reportType, isImageLoaded),
        state = markerState,
        title = pet.title,
        anchor = Offset(0.5f, 1.0f),
        onClick = { onClick(); true }
    ) {
        PetMarkerLayout(
            pet = pet,
            painter = painter,
            onClick = onClick
        )
    }
}

@Composable
private fun PetMarkerLayout(
    pet: ReportPost,
    painter: AsyncImagePainter,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val filterColor = Color(pet.reportType.colorHex)
        val defaultImage = painterResource(id = R.drawable.logo)

        Box(
            modifier = Modifier
                .size(56.dp)
                .border(3.dp, filterColor, CircleShape)
                .background(Color.White, CircleShape)
                .clip(CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    Image(
                        painter = defaultImage,
                        contentDescription = "Loading",
                        modifier = Modifier.size(24.dp),
                        alpha = 0.3f
                    )
                }
                is AsyncImagePainter.State.Error -> {
                    Image(
                        painter = defaultImage,
                        contentDescription = "Error",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
                else -> {
                    Image(
                        painter = painter,
                        contentDescription = "Pet Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.location_filled),
            contentDescription = null,
            colorFilter = ColorFilter.tint(filterColor),
            modifier = Modifier.size(32.dp)
        )
    }
}