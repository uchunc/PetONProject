package com.woo.peton.features.mypage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute // 🟢 [필수] Navigation Compose 확장 함수 import
import com.woo.peton.core.ui.navigation.MyPageNavigationRoute // 🟢 [필수] 라우트 클래스 import
import com.woo.peton.domain.model.MyPet
import com.woo.peton.domain.repository.MyPetRepository
import com.woo.peton.features.mypage.ui.state.MyPetDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPetDetailViewModel @Inject constructor(
    private val repository: MyPetRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // 🟢 [수정] Type-Safe Navigation 인수 복원 방식 변경
    // 기존: val petId = savedStateHandle["petId"] // ❌ null 가능성 높음
    // 변경: toRoute<T>()를 사용하여 객체 자체를 복원

    // (주의: MyPageNavigationRoute.MyPetDetailScreen 클래스의 프로퍼티명이 petId라고 가정)
    private val routeArgs = savedStateHandle.toRoute<MyPageNavigationRoute.MyPetDetailScreen>()
    private val petId: String = routeArgs.petId

    private val isNew = (petId == "new")

    private val _uiState = MutableStateFlow(MyPetDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // 초기 상태 설정
        _uiState.update {
            it.copy(
                isNewPet = isNew,
                isEditing = isNew, // 신규 등록이면 바로 수정 모드(true)
                isLoading = !isNew // 기존 펫이면 로딩 시작
            )
        }

        // 기존 펫 조회
        if (!isNew) {
            loadPet(petId)
        }
    }

    private fun loadPet(id: String) {
        viewModelScope.launch {
            val pet = repository.getPetById(id)
            if (pet != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        name = pet.name,
                        breed = pet.breed,
                        gender = pet.gender,
                        birthDate = pet.birthDate,
                        neutered = pet.neutered,
                        registrationNumber = pet.registrationNumber,
                        content = pet.content,
                        imageUrl = pet.imageUrl
                    )
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "정보를 불러올 수 없습니다.")
                }
            }
        }
    }

    // --- 사용자 액션 ---

    // 수정 모드로 전환
    fun startEditing() {
        _uiState.update { it.copy(isEditing = true) }
    }

    // 저장
    fun savePet() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val current = uiState.value

            val petToSave = MyPet(
                id = if (isNew) "" else petId,
                name = current.name,
                breed = current.breed,
                gender = current.gender,
                birthDate = current.birthDate,
                neutered = current.neutered,
                registrationNumber = current.registrationNumber,
                content = current.content,
                imageUrl = current.imageUrl
            )

            val result = repository.savePet(petToSave)

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        isSaveSuccess = true,
                        isEditing = false, // 편집 모드 종료 -> 보기 모드
                        isNewPet = false   // 신규였어도 이제는 저장됨
                    )
                }
            } else {
                _uiState.update { it.copy(isSaving = false, errorMessage = "저장 실패") }
            }
        }
    }

    // 입력값 업데이트 핸들러
    fun onNameChange(v: String) { _uiState.update { it.copy(name = v) } }
    fun onBreedChange(v: String) { _uiState.update { it.copy(breed = v) } }
    fun onGenderChange(v: String) { _uiState.update { it.copy(gender = v) } }
    fun onBirthDateChange(v: String) { _uiState.update { it.copy(birthDate = v) } }
    fun onNeuteredChange(v: Boolean) { _uiState.update { it.copy(neutered = v) } }
    fun onRegistrationNumberChange(v: String) { _uiState.update { it.copy(registrationNumber = v) } }
    fun onContentChange(v: String) { _uiState.update { it.copy(content = v) } }
    fun onImageChange(v: String) { _uiState.update { it.copy(imageUrl = v) } }
}