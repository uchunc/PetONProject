package com.woo.peton.features.missingreport

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
    private val reportPostRepository: ReportPostRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MissingReportUiState(isLoading = true))
    val uiState: StateFlow<MissingReportUiState> = _uiState.asStateFlow()

    private val _selectedPet = MutableStateFlow<MissingPet?>(null)
    val selectedPet: StateFlow<MissingPet?> = _selectedPet.asStateFlow()

    init {
        loadAllPets()
    }

    private fun loadAllPets() {
        viewModelScope.launch {
            // 3개의 데이터 스트림을 병렬로 구독하고 하나로 합침 (combine)
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

    /**
     * 필터 칩 클릭 시 filters 맵만 업데이트합니다.
     * UI State 내부의 currentPets 프로퍼티가 변경된 필터에 맞춰 자동으로 결과를 반환하게 됩니다.
     */
    fun toggleFilter(type: ReportType) {
        _uiState.update { currentState ->
            val newFilters = currentState.filters.toMutableMap().apply {
                this[type] = !(this[type] ?: true)
            }
            currentState.copy(filters = newFilters)
        }
    }

    /**
     * 지도 마커 클릭 시 선택된 펫 설정
     */
    fun selectPet(petId: String) {
        // 전체 목록(allPets)에서 해당 ID를 가진 펫을 찾습니다.
        val foundPet = uiState.value.allPets.find { it.id == petId }
        _selectedPet.value = foundPet
    }

    /**
     * 선택 해제 (바텀시트 닫기, 지도 빈 곳 클릭 등)
     */
    fun clearSelection() {
        _selectedPet.value = null
    }
}