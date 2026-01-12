package com.woo.peton.core.data.repository.impl

import com.woo.peton.domain.model.Detective
import com.woo.peton.core.ui.R
import com.woo.peton.domain.repository.DetectiveRepository
import javax.inject.Inject

internal class DetectiveRepositoryImpl @Inject constructor() : DetectiveRepository {

    override suspend fun getDetectives(): List<Detective> {
        return listOf(
            Detective(1, "애니몰스 119", R.drawable.logo),
            Detective(2, "SOS PET", R.drawable.logo)
        )
    }
}