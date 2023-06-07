package com.example.android_mapdiary.view.userInfo

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserInfoViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<UserInfoUiState> =
        MutableStateFlow(UserInfoUiState.None)
    val uiState = _uiState.asStateFlow()

    var name: String = ""
    var selectedImage: Bitmap? = null

    fun sendInfo() {
        _uiState.update { UserInfoUiState.Loading }
        viewModelScope.launch(Dispatchers.IO) {
            val result = UserRepository.saveInitUserInfo(name, selectedImage)
            if (result.isSuccess) {
                _uiState.update { UserInfoUiState.SuccessToSave }
            } else {
                _uiState.update { UserInfoUiState.FailedToSave(result.exceptionOrNull()!!) }
            }
        }
    }
}