package com.woo.peton.features.missingreport.ui.state

sealed class PostingUiState {
    object Idle : PostingUiState()
    object Loading : PostingUiState()
    object Success : PostingUiState()
    data class Error(val message: String) : PostingUiState()
}