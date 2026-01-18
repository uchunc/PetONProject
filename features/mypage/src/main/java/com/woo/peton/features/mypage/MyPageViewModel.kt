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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val myPetRepository: MyPetRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyPageUiState(isLoading = true))
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    init {
        loadMyPageData()
    }

    fun loadMyPageData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 유저 정보 가져오기
            authRepository.getUserProfile()
                .catch { e ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = "사용자 정보를 불러오지 못했습니다. (${e.message})"
                        )
                    }
                }
                .collect { user ->
                    if (user != null) {
                        try {
                            val pets = myPetRepository.getAllMyPets()

                            // 4. 상태 업데이트
                            _uiState.update { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    user = user,
                                    pets = pets,
                                    errorMessage = null
                                )
                            }
                        } catch (e: Exception) {
                            _uiState.update { it.copy(isLoading = false, errorMessage = "펫 정보를 불러오는데 실패했습니다.") }
                        }
                    } else {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                errorMessage = "로그인이 필요합니다."
                            )
                        }
                    }
                }
        }
    }

    fun logout() {
        authRepository.signOut()
    }
}