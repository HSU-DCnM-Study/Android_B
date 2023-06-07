package com.example.android_mapdiary.view.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.android_mapdiary.databinding.ItemProfilePostBinding
import com.example.android_mapdiary.view.home.postlist.PostItemUiState

class ProfilePostAdapter(
    private val onClickPost: (PostItemUiState) -> Unit
) : PagingDataAdapter<PostItemUiState, ProfilePostViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProfilePostBinding.inflate(layoutInflater, parent, false)
        return ProfilePostViewHolder(
            binding,
            onClickPost = onClickPost
        )
    }

    override fun onBindViewHolder(holder: ProfilePostViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<PostItemUiState>() {
            override fun areItemsTheSame(
                oldItem: PostItemUiState,
                newItem: PostItemUiState
            ): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(
                oldItem: PostItemUiState,
                newItem: PostItemUiState
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}