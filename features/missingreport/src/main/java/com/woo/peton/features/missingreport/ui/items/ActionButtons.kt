package com.woo.peton.features.missingreport.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.woo.peton.core.ui.R
import com.woo.peton.core.ui.component.CirclePlaceholder

@Composable
fun ActionButtons(
    modifier: Modifier = Modifier,
    onPostingClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onLocationClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CirclePlaceholder(
            modifier = Modifier.size(48.dp),
            color = Color(0xFFFF6F00),
            onClick = onPostingClick
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.plus),
                contentDescription = "작성",
                tint = Color.White
            )
        }

        CirclePlaceholder(
            modifier = Modifier.size(48.dp),
            color = Color.White,
            onClick = onFavoriteClick
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.book_mark_full),
                contentDescription = "즐겨찾기",
                tint = Color.Gray
            )
        }

        CirclePlaceholder(
            modifier = Modifier.size(48.dp),
            color = Color.White,
            onClick = onLocationClick
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.current_location),
                contentDescription = "현위치",
                tint = Color.Gray
            )
        }

    }
}