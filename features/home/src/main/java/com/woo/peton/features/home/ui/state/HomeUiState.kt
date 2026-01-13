package com.woo.peton.features.home.ui.state

import com.woo.peton.domain.model.Banner
import com.woo.peton.domain.model.Detective
import com.woo.peton.domain.model.MissingPet
import com.woo.peton.domain.model.MyPet

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val myPet: List<MyPet>,
        val missingPets: List<MissingPet>,
        val ProtectionPets: List<MissingPet>,
        val spottedPets: List<MissingPet>,
        val promoBanner: Banner?,
        val detectives: List<Detective>
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
}