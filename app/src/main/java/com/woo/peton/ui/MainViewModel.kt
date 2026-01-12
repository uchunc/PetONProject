package com.woo.peton.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel  : ViewModel() {
    private val _isLoading = MutableStateFlow(true)

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            // 1. 여기서 DataStore나 서버 통신을 통해 토큰 유효성을 검사합니다.
            // 예시: 2초 동안 로고 애니메이션을 보여주며 검사 대기
            delay(2000)

            // 2. 검사 결과 설정 (테스트를 위해 true/false 변경해보세요)
            val hasValidToken = true // 예: 토큰이 있으면 true

            _isLoggedIn.value = hasValidToken
            _isLoading.value = false
        }
    }
}