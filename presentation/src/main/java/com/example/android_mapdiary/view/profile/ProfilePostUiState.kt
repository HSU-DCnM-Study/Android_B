package com.example.android_mapdiary.view.profile

import androidx.paging.PagingData
import com.example.android_mapdiary.view.home.postlist.PostItemUiState
import com.example.domain.model.UserDetail

data class ProfilePostUiState(
    val pagingData: PagingData<PostItemUiState> = PagingData.empty()
)

data class ProfileDetailUiState(
    val userDetail: UserDetail? = null,
    val isLoading: Boolean = true,
    val userMessage: String? = null
)