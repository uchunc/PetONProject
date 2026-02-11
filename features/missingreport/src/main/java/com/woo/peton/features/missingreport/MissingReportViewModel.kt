package com.woo.peton.features.missingreport

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.woo.peton.domain.model.ReportPost
import com.woo.peton.domain.model.ReportType
import com.woo.peton.domain.repository.AuthRepository
import com.woo.peton.domain.repository.LocationRepository
import com.woo.peton.domain.repository.ReportPostRepository
import com.woo.peton.features.missingreport.ui.state.MissingReportUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MissingReportViewModel @Inject constructor(
    reportPostRepository: ReportPostRepository,
    locationRepository: LocationRepository,
    authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _filters = MutableStateFlow(ReportType.entries.associateWith { true })
    private val _selectedPet = MutableStateFlow<ReportPost?>(null)
    private val _loadedImageIds = MutableStateFlow<Set<String>>(emptySet())
    val isFromHome: Boolean = savedStateHandle.get<String>("filterType") != null
    val currentUserId: StateFlow<String?> = authRepository.getUserProfile()
        .map { it?.uid }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val currentLocation = locationRepository.latestLocation
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _allPetsFlow = combine(
        reportPostRepository.getPosts(ReportType.MISSING),
        reportPostRepository.getPosts(ReportType.PROTECTION),
        reportPostRepository.getPosts(ReportType.SPOTTED)
    ) { missing, protection, spotted ->
        missing + protection + spotted
    }
    private val _visibleBounds = MutableStateFlow<LatLngBounds?>(null)
    val uiState: StateFlow<MissingReportUiState> = combine(
        _allPetsFlow,
        _filters,
        _selectedPet,
        _visibleBounds
    ){ allPets, filters, selectedPet, bounds ->
        val filteredPets = allPets.filter { pet ->
            filters[pet.reportType] == true
        }

        val visiblePets = if (bounds == null) {
            filteredPets
        } else {
            filteredPets.filter { pet ->
                bounds.contains(LatLng(pet.latitude, pet.longitude))
            }
        }

        MissingReportUiState(
            pets = visiblePets,
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
        val foundPet = uiState.value.pets.find { it.id == petId }
        _selectedPet.value = foundPet
    }

    fun clearSelection() {
        _selectedPet.value = null
    }

    fun onImageLoaded(petId: String) {
        if (_loadedImageIds.value.contains(petId)) return
        _loadedImageIds.update { it + petId }
    }

    fun updateVisibleBounds(bounds: LatLngBounds) {
        _visibleBounds.value = bounds
    }
}