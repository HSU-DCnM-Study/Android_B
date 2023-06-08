package com.example.android_mapdiary.view.home.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_mapdiary.view.home.postlist.toUiState
import com.example.data.repository.AuthRepository
import com.example.data.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        MapUiState(currentUserUuid = requireNotNull(AuthRepository.currentUserUuid))
    )
    val uiState = _uiState.asStateFlow()

    private var bounded = false

    fun bind() {
        if (bounded) return
        bounded = true
        viewModelScope.launch(Dispatchers.IO) {
            val pagingFlow = PostRepository.getMapFeeds()
            _uiState.update { uiState ->
                uiState.copy(pagingData = pagingFlow.map { it.toUiState() })
            }
        }

    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}