package com.woo.peton.features.mypage.ui.state

import com.woo.peton.domain.model.MyPet

data class MyPetDetailUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val errorMessage: String? = null,

    // 화면 모드 제어
    val isEditing: Boolean = false, // true=수정모드, false=조회모드
    val isNewPet: Boolean = false,  // 신규 등록 여부

    // 데이터 필드 (MyPet 모델 반영)
    val name: String = "",
    val breed: String = "",
    val gender: String = "남",
    val birthDate: String = "",
    val neutered: Boolean = false,
    val registrationNumber: String = "", // 🟢 추가된 필드
    val content: String = "",          // 🟢 추가된 필드
    val imageUrl: String = ""
) {
    // 나이 텍스트 계산 (Model 로직 재사용)
    val ageText: String
        get() = MyPet(birthDate = birthDate, name = "", gender = "", breed = "").ageText
}