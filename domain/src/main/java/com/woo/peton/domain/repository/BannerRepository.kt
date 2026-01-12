package com.woo.peton.domain.repository

import com.woo.peton.domain.model.Banner


interface BannerRepository {
    suspend fun getPromoBanner(): Banner?
}