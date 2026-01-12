package com.woo.peton.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CirclePlaceholder(
    modifier: Modifier = Modifier,
    color: Color,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(color)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun CirclePlaceholder(
    color: Color,
    text: String,
    onClick: () -> Unit = {}
) {
    CirclePlaceholder(
        modifier = Modifier,
        color = color,
        onClick = onClick
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}