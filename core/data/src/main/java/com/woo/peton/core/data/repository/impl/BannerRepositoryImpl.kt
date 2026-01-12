package com.woo.peton.core.data.repository.impl

import androidx.compose.ui.graphics.Color
import com.woo.peton.domain.model.Banner
import com.woo.peton.core.ui.R
import com.woo.peton.domain.repository.BannerRepository
import javax.inject.Inject

internal class BannerRepositoryImpl @Inject constructor() : BannerRepository {

    override suspend fun getPromoBanner(): Banner? {
        return Banner(
            title = "10% 할인 쿠폰 증정!",
            description = "동물 탐정단 첫 이용 시",
            iconRes = R.drawable.logo,
            backgroundColor = Color(0xFFEFEBE9),
            onClick = { /* 로직 */ }
        )
    }
}