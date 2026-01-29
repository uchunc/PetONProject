package com.woo.peton.features.missingreport

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woo.peton.domain.model.ReportPost
import com.woo.peton.domain.model.ReportType
import com.woo.peton.domain.repository.AuthRepository
import com.woo.peton.domain.repository.ReportPostRepository
import com.woo.peton.features.missingreport.ui.state.PostingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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


    val reportType = MutableStateFlow(ReportType.MISSING)
    val selectedImageUri = MutableStateFlow<Uri?>(null)
    val title = MutableStateFlow("")
    val breed = MutableStateFlow("")
    val gender = MutableStateFlow("수컷")
    val age = MutableStateFlow("")
    val locationDescription = MutableStateFlow("")
    val latitude = MutableStateFlow(37.5665)//이후 기본값 현위치로 수정
    val longitude = MutableStateFlow(126.9780)
    val content = MutableStateFlow("")

    fun setLocation(name: String, lat: Double, lng: Double) {
        locationDescription.value = name
        latitude.value = lat
        longitude.value = lng
    }

    fun submitPost() {
        viewModelScope.launch {
            _uiState.value = PostingUiState.Loading

            val user = authRepository.getUserProfile().first()
            if (user == null) {
                _uiState.value = PostingUiState.Error("로그인 정보가 확인되지 않습니다.")
                return@launch
            }

            val finalImageUrl = selectedImageUri.value?.toString() ?: ""

            val newPost = ReportPost(
                reportType = reportType.value,
                title = title.value,
                breed = breed.value,
                gender = gender.value,
                age = age.value,
                locationDescription = locationDescription.value,
                content = content.value,

                imageUrl = finalImageUrl,
                latitude = latitude.value,
                longitude = longitude.value,

                occurrenceDate = LocalDateTime.now(),
                createdAt = LocalDateTime.now(),
                authorName = user.name,
                authorId = user.uid
            )
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