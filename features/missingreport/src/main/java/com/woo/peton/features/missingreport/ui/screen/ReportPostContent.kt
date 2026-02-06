package com.woo.peton.features.missingreport.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.woo.peton.core.utils.toFormattedString
import com.woo.peton.core.utils.toRelativeString
import com.woo.peton.domain.model.ReportPost
import com.woo.peton.features.missingreport.ui.items.map.marker.PetMarker

@Composable
fun ReportPostContent(
    pet: ReportPost,
    modifier: Modifier = Modifier,
    contentMaxLines: Int = Int.MAX_VALUE,
    showMap: Boolean = true
) {
    Column(modifier = modifier.padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = Color(pet.reportType.colorHex),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = pet.reportType.label,
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = pet.occurrenceDate.toFormattedString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = pet.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = pet.authorName,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "·  ${pet.createdAt.toRelativeString()}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 24.dp),
            color = Color(0xFFEEEEEE)
        )

        InfoRow(label = "품종", value = pet.breed)
        InfoRow(label = "성별 / 나이", value = "${pet.gender} / ${pet.age}")
        InfoRow(label = "발견/실종 장소", value = pet.locationDescription)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "상세 내용",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = pet.content,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF424242),
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.5,
            maxLines = contentMaxLines,
            overflow = TextOverflow.Ellipsis
        )

        when (showMap) {
            true -> {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "위치 정보",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                DetailMapSection(pet = pet)
            }
            false -> Unit
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun DetailMapSection(pet: ReportPost) {
    val location = LatLng(pet.latitude, pet.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 16f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                scrollGesturesEnabled = false,
                zoomGesturesEnabled = false,
                mapToolbarEnabled = false
            )
        ) {
            PetMarker(
                pet = pet,
                showImage = false,
                onClick = {}
            )
        }
    }
}