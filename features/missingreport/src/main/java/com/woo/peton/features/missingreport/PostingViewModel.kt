package com.woo.peton.features.missingreport

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woo.peton.domain.model.MissingPet
import com.woo.peton.domain.model.ReportType
import com.woo.peton.domain.repository.AuthRepository
import com.woo.peton.domain.repository.ReportPostRepository
import com.woo.peton.features.missingreport.ui.state.PostingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PostingViewModel @Inject constructor(
    private val reportPostRepository: ReportPostRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostingUiState>(PostingUiState.Idle)
    val uiState: StateFlow<PostingUiState> = _uiState.asStateFlow()

    // --- 입력 필드 상태 ---
    val reportType = MutableStateFlow(ReportType.MISSING)
    val selectedImageUri = MutableStateFlow<Uri?>(null)
    val title = MutableStateFlow("")
    val breed = MutableStateFlow("")
    val gender = MutableStateFlow("수컷") // 기본값
    val age = MutableStateFlow("")
    val locationDescription = MutableStateFlow("")
    val latitude = MutableStateFlow(37.5665) // 기본값: 서울시청
    val longitude = MutableStateFlow(126.9780)
    val content = MutableStateFlow("")

    fun setLocation(name: String, lat: Double, lng: Double) {
        locationDescription.value = name
        latitude.value = lat
        longitude.value = lng
    }
    // 게시글 등록 요청
    fun submitPost() {
        viewModelScope.launch {
            _uiState.value = PostingUiState.Loading

            // 1. 작성자 정보 가져오기
            val userResult = authRepository.getUserProfile()
            if (userResult.isFailure) {
                _uiState.value = PostingUiState.Error("로그인 정보가 확인되지 않습니다.")
                return@launch
            }
            val user = userResult.getOrThrow()

            // ⚠️ 실제 앱에서는 여기서 Firebase Storage에 selectedImageUri를 업로드하고
            // 다운로드 받은 URL을 imageUrl에 넣어야 합니다.
            // 현재는 테스트를 위해 랜덤 이미지 URL을 사용합니다.
            val finalImageUrl = if (selectedImageUri.value != null) {
                // 업로드 로직이 구현되면 여기에 실제 URL이 들어감
                "https://placedog.net/500?random=${System.currentTimeMillis()}"
            } else {
                "https://placedog.net/500?random=${System.currentTimeMillis()}"
            }

            // 2. MissingPet 객체 생성
            val newPost = MissingPet(
                reportType = reportType.value,
                title = title.value,
                breed = breed.value,
                gender = gender.value,
                age = age.value,
                locationDescription = locationDescription.value,
                content = content.value,

                imageUrl = finalImageUrl,
                latitude = latitude.value,     // 🟢 선택된 좌표 사용
                longitude = longitude.value,

                occurrenceDate = LocalDateTime.now(),
                createdAt = LocalDateTime.now(),
                authorName = user.name,
                authorId = user.uid
            )

            // 3. Repository 호출
            val result = reportPostRepository.addPost(newPost)

            if (result.isSuccess) {
                _uiState.value = PostingUiState.Success
            } else {
                _uiState.value = PostingUiState.Error("게시글 등록에 실패했습니다.")
            }
        }
    }

    fun resetState() {
        _uiState.value = PostingUiState.Idle
        selectedImageUri.value = null
        title.value = ""
        breed.value = ""
        age.value = ""
        locationDescription.value = ""
        content.value = ""
    }
}