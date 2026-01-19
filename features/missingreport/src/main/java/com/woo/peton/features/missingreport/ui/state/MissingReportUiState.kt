package com.woo.peton.features.missingreport.ui.state

import com.woo.peton.domain.model.MissingPet
import com.woo.peton.domain.model.ReportType

data class MissingReportUiState(
    val currentPets: List<MissingPet> = emptyList(),
    val selectedPet: MissingPet? = null,
    val filters: Map<ReportType, Boolean> = ReportType.entries.associateWith { true },
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)