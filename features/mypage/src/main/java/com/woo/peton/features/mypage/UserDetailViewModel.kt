package com.woo.peton.features.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woo.peton.domain.model.User
import com.woo.peton.domain.repository.AuthRepository
import com.woo.peton.features.mypage.ui.state.UserDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserDetailUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val user = authRepository.getUserProfile().first()

            _uiState.update { state ->
                if (user != null) {
                    state.copy(
                        isLoading = false,
                        uid = user.uid,
                        name = user.name,
                        email = user.email,
                        phoneNumber = user.phoneNumber,
                        address = user.address ?: "",
                        profileImageUrl = user.profileImageUrl ?: ""
                    )
                } else {
                    state.copy(isLoading = false, errorMessage = "유저 정보를 불러올 수 없습니다.")
                }
            }
        }
    }

    fun startEditing() {
        _uiState.update { it.copy(isEditing = true) }
    }

    fun saveProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val current = uiState.value

            val userToSave = User(
                uid = current.uid,
                email = current.email,
                name = current.name,
                phoneNumber = current.phoneNumber,
                address = current.address,
                profileImageUrl = current.profileImageUrl
            )
            authRepository.updateUserProfile(userToSave)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            isSaveSuccess = true,
                            isEditing = false
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = "저장 실패: ${e.message}"
                        )
                    }
                }
        }
    }

    fun onNameChange(v: String) = _uiState.update { it.copy(name = v) }
    fun onPhoneChange(v: String) = _uiState.update { it.copy(phoneNumber = v) }
    fun onAddressChange(v: String) = _uiState.update { it.copy(address = v) }
    fun onImageChange(v: String) = _uiState.update { it.copy(profileImageUrl = v) }
}