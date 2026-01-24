package com.woo.peton.features.missingreport.ui.items

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.woo.peton.core.ui.R
import com.woo.peton.core.ui.component.CirclePlaceholder

@Composable
fun CurrentLocationButton(
    onLocationClick: () -> Unit
) {
    CirclePlaceholder(
        modifier = Modifier.size(48.dp),
        color = MaterialTheme.colorScheme.onPrimary,
        elevation = 2.dp,
        onClick = onLocationClick
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.current_location),
            contentDescription = "현위치",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}