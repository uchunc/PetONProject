package com.woo.peton.features.missingreport.ui.items.map.marker

import android.graphics.Bitmap
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.woo.peton.core.ui.R
import com.woo.peton.domain.model.ReportPost

@Composable
fun PetMarker(
    pet: ReportPost,
    isImageLoaded: Boolean = true,
    onImageLoaded: () -> Unit ={},
    showImage: Boolean = true,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    var imageBitmap by remember(pet.id) { mutableStateOf<Bitmap?>(null) }

    if (showImage) {
        LaunchedEffect(pet.imageUrl) {
            if (imageBitmap != null) return@LaunchedEffect

            val request = ImageRequest.Builder(context)
                .data(pet.imageUrl.takeIf { it.isNotEmpty() })
                .size(with(density) { 56.dp.roundToPx() })
                .scale(Scale.FIT)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .memoryCacheKey(pet.id)
                .allowHardware(false)
                .build()

            val result = context.imageLoader.execute(request)

            if (result is SuccessResult) {
                imageBitmap = result.drawable.toBitmap()
                onImageLoaded()
            }
        }
    }

    val markerState = remember(pet.id) {
        MarkerState(position = LatLng(pet.latitude, pet.longitude))
    }.apply{
        position = LatLng(pet.latitude, pet.longitude)
    }

    val isLoaded = imageBitmap != null
    val markerKeys = when (showImage) {
        true -> arrayOf<Any>(pet.id, pet.reportType, isLoaded)
        false -> arrayOf<Any>(pet.id, pet.reportType, "simple")
    }

    MarkerComposable(
        keys = markerKeys,
        state = markerState,
        title = pet.title,
        anchor = Offset(0.5f, 1.0f),
        onClick = { onClick(); true }
    ) {
        PetMarkerLayout(
            pet = pet,
            imageBitmap = imageBitmap,
            showImage = showImage,
            onClick = onClick
        )
    }
}

@Composable
private fun PetMarkerLayout(
    pet: ReportPost,
    imageBitmap: Bitmap?,
    showImage: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val filterColor = Color(pet.reportType.colorHex)
        val defaultImage = painterResource(id = R.drawable.logo)

        if (showImage) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .border(3.dp, filterColor, CircleShape)
                    .background(Color.White, CircleShape)
                    .clip(CircleShape)
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap.asImageBitmap(),
                        contentDescription = "Pet Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = defaultImage,
                        contentDescription = "Loading",
                        modifier = Modifier.size(24.dp),
                        alpha = 0.3f
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