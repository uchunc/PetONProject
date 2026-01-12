package com.woo.peton.features.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woo.peton.domain.repository.AuthRepository
import com.woo.peton.domain.repository.MyPetRepository
import com.woo.peton.features.mypage.ui.state.MyPageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val myPetRepository: MyPetRepository
) : ViewModel() {

    // 1. UI 상태 관리 (Data Class 초기값 사용)
    private val _uiState = MutableStateFlow(MyPageUiState(isLoading = true))
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    init {
        loadMyPageData()
    }

    fun loadMyPageData() {
        viewModelScope.launch {
            // 로딩 시작
            _uiState.update { it.copy(isLoading = true) }

            // 유저 정보 가져오기
            val userResult = authRepository.getUserProfile()

            if (userResult.isSuccess) {
                // 펫 목록 가져오기
                val pets = myPetRepository.getAllMyPets()

                // 성공 시 상태 업데이트
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        user = userResult.getOrThrow(),
                        pets = pets,
                        errorMessage = null
                    )
                }
            } else {
                // 실패 시 에러 메시지 업데이트
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "사용자 정보를 불러오지 못했습니다."
                    )
                }
            }
        }
    }

    fun logout() {
        authRepository.signOut()
        // 로그아웃 후 처리는 UI 레벨의 콜백이나 별도 Event Flow를 통해 처리
    }
}