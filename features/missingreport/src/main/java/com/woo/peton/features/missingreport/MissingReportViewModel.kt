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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MissingReportViewModel @Inject constructor(
    private val reportPostRepository: ReportPostRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(MissingReportUiState(isLoading = true))
    val uiState: StateFlow<MissingReportUiState> = _uiState.asStateFlow()

    private val _selectedPet = MutableStateFlow<MissingPet?>(null)
    val selectedPet: StateFlow<MissingPet?> = _selectedPet.asStateFlow()

    val isFromHome: Boolean = savedStateHandle.get<String>("filterType") != null

    init {
        loadAllPets()
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

    private fun loadAllPets() {
        viewModelScope.launch {
            combine(
                reportPostRepository.getPosts(ReportType.MISSING),
                reportPostRepository.getPosts(ReportType.PROTECTION),
                reportPostRepository.getPosts(ReportType.SPOTTED)
            ) { missing, protection, spotted ->
                missing + protection + spotted
            }
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .catch { e ->
                    e.printStackTrace()
                    _uiState.update { it.copy(isLoading = false) }
                }
                .collect { combinedPets ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            allPets = combinedPets,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun updateFilter(targetType: ReportType, clearOthers: Boolean = false) {
        _uiState.update { currentState ->
            val newFilters = currentState.filters.toMutableMap().apply {
                if (clearOthers) {
                    keys.forEach { key ->
                        this[key] = (key == targetType)
                    }
                } else {
                    this[targetType] = !(this[targetType] ?: false)
                }
            }
            currentState.copy(filters = newFilters)
        }
    }

    fun selectPet(petId: String) {
        // 전체 목록(allPets)에서 해당 ID를 가진 펫을 찾습니다.
        val foundPet = uiState.value.allPets.find { it.id == petId }
        _selectedPet.value = foundPet
    }

    fun clearSelection() {
        _selectedPet.value = null
    }
}