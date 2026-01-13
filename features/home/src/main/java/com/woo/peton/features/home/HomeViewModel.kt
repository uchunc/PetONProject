package com.woo.peton.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woo.peton.domain.model.ReportType
import com.woo.peton.domain.repository.BannerRepository
import com.woo.peton.domain.repository.DetectiveRepository
import com.woo.peton.domain.repository.MyPetRepository
import com.woo.peton.domain.repository.ReportPostRepository
import com.woo.peton.features.home.ui.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val myPetRepository: MyPetRepository,
    private val bannerRepository: BannerRepository,
    private val reportPostRepository: ReportPostRepository,
    private val detectiveRepository: DetectiveRepository
) : ViewModel() {

    // combine을 사용하여 5개의 데이터 소스를 하나로 합칩니다.
    val uiState: StateFlow<HomeUiState> = combine(
        // 1. 내 반려동물 (suspend 함수라면 flow {}로 감쌉니다)
        flow { emit(myPetRepository.getAllMyPets()) },

        // 2. 실종 동물 (이미 Flow를 반환하므로 그대로 사용)
        reportPostRepository.getPosts(ReportType.MISSING),

        // 3. 임보 동물 (이미 Flow를 반환하므로 그대로 사용)
        reportPostRepository.getPosts(ReportType.PROTECTION),

        // 4. 배너 (suspend 함수 가정)
        flow { emit(bannerRepository.getPromoBanner()) },

        // 5. 탐정 리스트 (suspend 함수 가정)
        flow { emit(detectiveRepository.getDetectives()) }

    ) { myPets, missingPets, fosterPets, promoBanner, detectives ->
        // 5개의 데이터가 모두 도착하거나, 업데이트될 때마다 이 블록이 실행됩니다.
        HomeUiState.Success(
            myPet = myPets,
            missingPets = missingPets,
            ProtectionPets = fosterPets,
            promoBanner = promoBanner,
            detectives = detectives
        ) as HomeUiState // 타입 캐스팅 명시
    }
        .onStart {
            emit(HomeUiState.Loading)
        }
        .catch { e ->
            e.printStackTrace()
            emit(HomeUiState.Error(e.message ?: "데이터를 불러오는 중 오류가 발생했습니다."))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading
        )
}