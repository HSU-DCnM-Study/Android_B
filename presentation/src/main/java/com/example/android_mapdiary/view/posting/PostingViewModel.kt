package com.example.android_mapdiary.view.posting

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_mapdiary.R
import com.example.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PostingUiState())
    val uiState = _uiState.asStateFlow()

    fun selectImage(uri: Uri) {
        _uiState.update { it.copy(selectedImage = uri) }
    }

    fun settingLocation(latitude: Double, longitude: Double) {
        _uiState.update {
            it.copy(
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    fun changeToEditMode() {
        _uiState.update { it.copy(isCreating = false) }
    }

    fun uploadContent(content: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = PostRepository.uploadPost(
                content = content,
                imageUri = uiState.value.selectedImage!!,
                latitude = uiState.value.latitude!!,
                longitude = uiState.value.longitude!!
            )
            if (result.isSuccess) {
                _uiState.update { it.copy(successToUpload = true, isLoading = false) }
            } else {
                _uiState.update {
                    it.copy(
                        userMessage = R.string.failed_to_upload,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun editContent(uuid: String, content: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result: Result<Unit> = if (_uiState.value.selectedImage != null) {
                PostRepository.editPost(uuid, content, uiState.value.selectedImage!!)
            } else {
                PostRepository.editPostOnlyContent(uuid, content)
            }
            if (result.isSuccess) {
                _uiState.update { it.copy(successToUpload = true, isLoading = false) }
            } else {
                _uiState.update {
                    it.copy(
                        userMessage = R.string.failed_to_upload,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}