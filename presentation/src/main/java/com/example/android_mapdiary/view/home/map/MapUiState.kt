package com.example.android_mapdiary.view.home.map

import androidx.annotation.StringRes
import androidx.paging.PagingData
import com.example.android_mapdiary.view.home.postlist.PostItemUiState

data class MapUiState(
    val pagingData: PagingData<PostItemUiState> = PagingData.empty(),
    val selectedPostItem: PostItemUiState? = null,
    val currentUserUuid: String,
    @StringRes
    val userMessage: Int? = null
)