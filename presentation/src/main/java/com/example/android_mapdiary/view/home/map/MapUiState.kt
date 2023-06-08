package com.example.android_mapdiary.view.home.map

import androidx.annotation.StringRes
import com.example.android_mapdiary.view.home.postlist.PostItemUiState

data class MapUiState(
    val pagingData: List<PostItemUiState> = emptyList(),
    val selectedPostItem: PostItemUiState? = null,
    val currentUserUuid: String,
    @StringRes
    val userMessage: Int? = null
)