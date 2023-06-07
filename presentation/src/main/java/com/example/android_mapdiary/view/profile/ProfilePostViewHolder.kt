package com.example.android_mapdiary.view.profile

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_mapdiary.databinding.ItemProfilePostBinding
import com.example.android_mapdiary.view.home.postlist.PostItemUiState
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfilePostViewHolder(
    private val binding: ItemProfilePostBinding,
    private val onClickPost: (PostItemUiState) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val storageReference = Firebase.storage.reference

    fun bind(uiState: PostItemUiState) = with(binding) {
        val glide = Glide.with(root)

        glide.load(storageReference.child(uiState.imageUrl))
            .override(200, 200)
            .into(profilePostImage)

        root.setOnClickListener {
            onClickPost(uiState)
        }
    }
}