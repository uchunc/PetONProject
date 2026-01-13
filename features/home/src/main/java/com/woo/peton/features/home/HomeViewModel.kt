package com.woo.peton.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woo.peton.domain.model.MissingPet
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

    private data class PetPosts(
        val missing: List<MissingPet>,
        val protection: List<MissingPet>,
        val spotted: List<MissingPet>
    )

    private val petPostsFlow = combine(
        reportPostRepository.getPosts(ReportType.MISSING),
        reportPostRepository.getPosts(ReportType.PROTECTION),
        reportPostRepository.getPosts(ReportType.SPOTTED)
    ) { missing, protection, spotted ->
        PetPosts(missing, protection, spotted)
    }

    val uiState: StateFlow<HomeUiState> = combine(
        flow { emit(myPetRepository.getAllMyPets()) },

        petPostsFlow,

        flow { emit(bannerRepository.getPromoBanner()) },

        flow { emit(detectiveRepository.getDetectives()) }

    ) { myPets, posts, promoBanner, detectives ->
        HomeUiState.Success(
            myPet = myPets,
            missingPets = posts.missing,
            protectionPets = posts.protection,
            spottedPets = posts.spotted,
            promoBanner = promoBanner,
            detectives = detectives
        ) as HomeUiState
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