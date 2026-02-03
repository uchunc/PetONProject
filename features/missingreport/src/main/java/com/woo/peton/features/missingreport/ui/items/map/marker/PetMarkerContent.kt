package com.woo.peton.features.missingreport.ui.items.map.marker

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.woo.peton.core.ui.R
import com.woo.peton.core.ui.component.CirclePlaceholder
import com.woo.peton.domain.model.ReportPost

@Composable
fun PetMarkerContent(
    pet: ReportPost,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(60.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.location_filled),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color(pet.reportType.colorHex)),
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.BottomCenter)
        )

        val borderColor = Color(pet.reportType.colorHex)

        CirclePlaceholder(
            modifier = Modifier
                .size(36.dp)
                .align(Alignment.TopCenter)
                .border(2.dp, borderColor, CircleShape),
            color = Color.White,
            elevation = 4.dp,
            onClick = onClick
        ) {
            if (pet.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(pet.imageUrl)
                        .allowHardware(false) // 마커 렌더링용 소프트웨어 비트맵 설정
                        .build(),
                    contentDescription = "Pet Image",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}