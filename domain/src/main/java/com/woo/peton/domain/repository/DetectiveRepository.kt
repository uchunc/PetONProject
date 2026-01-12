package com.woo.peton.domain.repository

import com.woo.peton.domain.model.Detective


interface DetectiveRepository {
    suspend fun getDetectives(): List<Detective>
}