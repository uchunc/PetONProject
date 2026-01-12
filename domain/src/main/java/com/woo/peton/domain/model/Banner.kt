package com.woo.peton.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class Banner(
    val title: String,
    val description: String,
    @param:DrawableRes val iconRes: Int? = null,
    val backgroundColor: Color = Color.White,
    val onClick: () -> Unit = {}
)