package com.woo.peton.features.missingreport

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woo.peton.domain.model.MissingPet
import com.woo.peton.domain.model.ReportType
import com.woo.peton.domain.repository.ReportPostRepository
import com.woo.peton.features.missingreport.ui.state.MissingReportUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MissingReportViewModel @Inject constructor(
    reportPostRepository: ReportPostRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _filters = MutableStateFlow(ReportType.entries.associateWith { true })
    private val _selectedPet = MutableStateFlow<MissingPet?>(null)

    val isFromHome: Boolean = savedStateHandle.get<String>("filterType") != null

    private val _allPetsFlow = combine(
        reportPostRepository.getPosts(ReportType.MISSING),
        reportPostRepository.getPosts(ReportType.PROTECTION),
        reportPostRepository.getPosts(ReportType.SPOTTED)
    ) { missing, protection, spotted ->
        missing + protection + spotted
    }

    val uiState: StateFlow<MissingReportUiState> = combine(
        _allPetsFlow,
        _filters,
        _selectedPet
    ){ allPets, filters, selectedPet ->
        val filteredPets = allPets.filter { pet ->
            filters[pet.reportType] == true
        }

        MissingReportUiState(
            currentPets = filteredPets,
            selectedPet = selectedPet,
            filters = filters,
            isLoading = false,
            errorMessage = null
        )
    }
        .onStart {
            emit(MissingReportUiState(isLoading = true))
        }
        .catch { e ->
            e.printStackTrace()
            emit(MissingReportUiState(errorMessage = "데이터를 불러오는 중 오류가 발생했습니다."))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MissingReportUiState(isLoading = true)
        )

    init {
        val typeString: String? = savedStateHandle["filterType"]
        if (typeString != null) {
            try {
                val type = ReportType.valueOf(typeString)
                updateFilter(type, clearOthers = true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateFilter(targetType: ReportType, clearOthers: Boolean = false) {
        _filters.update { currentFilters ->
            if (clearOthers) {
                currentFilters.keys.associateWith { it == targetType }
            } else {
                currentFilters.toMutableMap().apply {
                    this[targetType] = !(this[targetType] ?: false)
                }
            }
        }
    }

    fun selectPet(petId: String) {
        val foundPet = uiState.value.currentPets.find { it.id == petId }
        _selectedPet.value = foundPet
    }

    fun clearSelection() {
        _selectedPet.value = null
    }
}