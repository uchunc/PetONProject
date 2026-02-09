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
import androidx.core.net.toUri

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
    val animalType = MutableStateFlow("개")
    val breed = MutableStateFlow("")
    val gender = MutableStateFlow("수컷")
    val age = MutableStateFlow("")
    val locationDescription = MutableStateFlow("")
    val latitude = MutableStateFlow(37.5665)
    val longitude = MutableStateFlow(126.9780)
    val content = MutableStateFlow("")

    private var originalCreatedAt: LocalDateTime? = null
    private var editingPostId: String? = null

    fun initForEdit(post: ReportPost) {
        editingPostId = post.id
        originalCreatedAt = post.createdAt

        reportType.value = post.reportType
        title.value = post.title
        animalType.value = post.animalType
        breed.value = post.breed
        gender.value = post.gender
        age.value = post.age
        locationDescription.value = post.locationDescription
        latitude.value = post.latitude
        longitude.value = post.longitude
        content.value = post.content

        if (post.imageUrl.isNotEmpty()) {
            selectedImageUri.value = post.imageUrl.toUri()
        }
    }

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

            if (editingPostId != null) {
                updateExistingPost(editingPostId!!, user.uid, user.name, finalImageUrl)
            } else {
                createNewPost(user.uid, user.name, finalImageUrl)
            }
        }
    }

    private suspend fun createNewPost(userId: String, userName: String, imageUrl: String) {
        val newPost = ReportPost(
            reportType = reportType.value,
            title = title.value,
            authorId = userId,
            animalType = animalType.value,
            breed = breed.value,
            gender = gender.value,
            age = age.value,
            locationDescription = locationDescription.value,
            content = content.value,
            imageUrl = imageUrl,
            latitude = latitude.value,
            longitude = longitude.value,
            occurrenceDate = LocalDateTime.now(),
            createdAt = LocalDateTime.now(),
            authorName = userName
        )

        val result = reportPostRepository.addPost(newPost)
        handleResult(result)
    }

    private suspend fun updateExistingPost(postId: String, userId: String, userName: String, imageUrl: String) {
        val updatedPost = ReportPost(
            id = postId,
            reportType = reportType.value,
            title = title.value,
            animalType = animalType.value,
            breed = breed.value,
            gender = gender.value,
            age = age.value,
            locationDescription = locationDescription.value,
            content = content.value,
            imageUrl = imageUrl,
            latitude = latitude.value,
            longitude = longitude.value,
            occurrenceDate = LocalDateTime.now(),
            createdAt = originalCreatedAt ?: LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            authorName = userName,
            authorId = userId
        )
        val result = reportPostRepository.updatePost(updatedPost)
        handleResult(result)
    }

    private fun handleResult(result: Result<Boolean>) {
        if (result.isSuccess) {
            _uiState.value = PostingUiState.Success
        } else {
            _uiState.value = PostingUiState.Error("작업 실패")
        }
    }

    fun resetState() {
        editingPostId = null
        _uiState.value = PostingUiState.Idle
        selectedImageUri.value = null
        animalType.value = "개"
        title.value = ""
        breed.value = ""
        age.value = ""
        locationDescription.value = ""
        content.value = ""
    }
}