package com.woo.peton.features.missingreport.ui.state

import com.woo.peton.domain.model.ReportPost
import com.woo.peton.domain.model.ReportType

data class MissingReportUiState(
    val currentPets: List<ReportPost> = emptyList(),
    val selectedPet: ReportPost? = null,
    val filters: Map<ReportType, Boolean> = ReportType.entries.associateWith { true },
    val loadedImageIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)