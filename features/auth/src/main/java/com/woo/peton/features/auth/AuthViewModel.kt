package com.woo.peton.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woo.peton.domain.model.MyPet
import com.woo.peton.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI 상태 정의
sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // --- 회원가입용 유저 정보 임시 저장소 ---
    var signUpEmail = ""
    var signUpPassword = ""
    var signUpName = ""
    var signUpPhone = ""

    // --- 회원가입용 펫 정보 임시 저장소 (MyPet 모델 필드 반영) ---
    var signUpPetName = ""
    var signUpPetBreed = ""
    var signUpPetGender = "남"        // 기본값 설정
    var signUpPetBirthDate = ""     // "YYYY-MM-DD" 형식
    var signUpPetNeutered = false   // 중성화 여부
    var signUpPetRegNumber = ""     // 동물등록번호 (선택)
    var signUpPetContent = ""       // 특징 (선택)
    var signUpPetImage = ""         // 이미지 URL (선택)

    // 1. 로그인 함수
    fun login(email: String, pw: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.signIn(email, pw)
            result.onSuccess {
                _uiState.value = AuthUiState.Success
            }.onFailure {
                _uiState.value = AuthUiState.Error(it.message ?: "로그인 실패")
            }
        }
    }

    // 2. 회원가입 완료 함수 (Step 4 진입 시 호출)
    fun requestSignUp() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading

            // 임시 저장된 데이터들을 모아서 전송
            // MyPet 모델의 변경된 필드들을 모두 포함하여 생성
            val petData = MyPet(
                id = "", // Repository/Firebase에서 생성됨
                ownerId = "", // Repository에서 할당됨
                name = signUpPetName,
                breed = signUpPetBreed,
                gender = signUpPetGender,
                birthDate = signUpPetBirthDate,
                neutered = signUpPetNeutered,
                registrationNumber = signUpPetRegNumber,
                content = signUpPetContent,
                imageUrl = signUpPetImage
            )

            val result = authRepository.signUp(
                email = signUpEmail,
                pw = signUpPassword,
                name = signUpName,
                phone = signUpPhone,
                petInfo = petData
            )

            result.onSuccess {
                _uiState.value = AuthUiState.Success
            }.onFailure {
                _uiState.value = AuthUiState.Error(it.message ?: "회원가입 실패")
            }
        }
    }

    // 상태 초기화 (화면 이동 시 등)
    fun resetState() {
        _uiState.value = AuthUiState.Idle
        // 필요하다면 입력 필드들도 여기서 초기화 가능
    }
}