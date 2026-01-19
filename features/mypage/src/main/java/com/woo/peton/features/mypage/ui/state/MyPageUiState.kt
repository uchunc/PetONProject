package com.woo.peton.features.mypage.ui.state

import com.woo.peton.domain.model.MyPet
import com.woo.peton.domain.model.User

data class MyPageUiState(
    val isLoading: Boolean = false,
    val user: User = User(),
    val pets: List<MyPet> = emptyList(),
    val errorMessage: String? = null
)