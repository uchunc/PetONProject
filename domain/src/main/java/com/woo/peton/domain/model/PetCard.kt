package com.woo.peton.domain.model

import androidx.annotation.DrawableRes

data class PetCard(
    val id: Long,
    val title: String,       // 예: "검정 치와와"
    val description: String, // 예: "행동에서 발견..."
    @DrawableRes val imageRes: Int // 나중에 String(URL)로 변경됨
)