package com.woo.peton.features.mypage.ui.state

import com.woo.peton.domain.model.MyPet
import com.woo.peton.domain.model.User

data class MyPageUiState(
    val isLoading: Boolean = false,
    val user: User = User(), // 초기값 설정
    val pets: List<MyPet> = emptyList(),
    val errorMessage: String? = null
)