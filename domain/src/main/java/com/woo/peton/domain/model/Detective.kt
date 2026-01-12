package com.woo.peton.domain.model

import androidx.annotation.DrawableRes

data class Detective(
    val id: Long,
    val name: String,
    @DrawableRes val imageRes: Int
)