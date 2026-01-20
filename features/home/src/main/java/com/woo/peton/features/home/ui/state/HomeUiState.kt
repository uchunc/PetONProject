package com.woo.peton.features.home.ui.state

import com.woo.peton.domain.model.Banner
import com.woo.peton.domain.model.Detective
import com.woo.peton.domain.model.ReportPost
import com.woo.peton.domain.model.MyPet

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val myPet: List<MyPet>,
        val reportPosts: List<ReportPost>,
        val protectionPets: List<ReportPost>,
        val spottedPets: List<ReportPost>,
        val promoBanner: Banner?,
        val detectives: List<Detective>
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
}