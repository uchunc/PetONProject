package com.woo.peton.features.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woo.peton.domain.model.User
import com.woo.peton.domain.repository.AuthRepository
import com.woo.peton.domain.repository.MyPetRepository
import com.woo.peton.features.mypage.ui.state.MyPageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
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

            combine(
                authRepository.getUserProfile(),
                myPetRepository.getAllMyPets()
            ) { user, pets ->
                if (user != null) {
                    MyPageUiState(
                        isLoading = false,
                        user = user,
                        pets = pets,
                        errorMessage = null
                    )
                } else {
                    MyPageUiState(
                        isLoading = false,
                        user = User(),
                        pets = emptyList(),
                        errorMessage = "로그인이 필요합니다."
                    )
                }
            }.catch { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "데이터를 불러오는 중 오류가 발생했습니다. (${e.message})"
                    )
                }
            }
            .collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun logout() {
        authRepository.signOut()
    }
}