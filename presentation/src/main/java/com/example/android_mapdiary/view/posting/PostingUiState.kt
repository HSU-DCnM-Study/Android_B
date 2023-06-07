package com.example.android_mapdiary.view.posting

import android.net.Uri
import androidx.annotation.StringRes

data class PostingUiState(
    val selectedImage: Uri? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @StringRes
    val userMessage: Int? = null,
    val isCreating: Boolean = true,
    val isLoading: Boolean = false,
    val successToUpload: Boolean = false
)