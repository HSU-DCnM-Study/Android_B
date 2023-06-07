package com.example.android_mapdiary.view.userInfo

sealed class UserInfoUiState {
    object None : UserInfoUiState()
    object Loading : UserInfoUiState()
    object SuccessToSave : UserInfoUiState()
    data class FailedToSave(val exception: Throwable) : UserInfoUiState()
}
