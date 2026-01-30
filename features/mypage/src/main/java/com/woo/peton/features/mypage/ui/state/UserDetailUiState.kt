package com.woo.peton.features.mypage.ui.state

data class UserDetailUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val errorMessage: String? = null,

    val isEditing: Boolean = false,

    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val profileImageUrl: String = ""
)