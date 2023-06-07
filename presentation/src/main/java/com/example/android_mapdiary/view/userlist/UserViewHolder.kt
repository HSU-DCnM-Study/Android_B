package com.example.android_mapdiary.view.userlist

import androidx.recyclerview.widget.RecyclerView
import com.example.android_mapdiary.R
import com.example.android_mapdiary.databinding.ItemUserBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class UserViewHolder(
    private val binding: ItemUserBinding,
    private val onClickUser: (UserItemUiState) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val storageReference = Firebase.storage.reference

    fun bind(uiState: UserItemUiState) = with(binding) {
        val glide = com.bumptech.glide.Glide.with(root)

        glide.load(uiState.profileImageUrl?.let { storageReference.child(it) })
            .fallback(R.drawable.ic_baseline_person_24)
            .into(profileImage)

        name.text = uiState.name

        root.setOnClickListener {
            onClickUser(uiState)
        }
    }
}