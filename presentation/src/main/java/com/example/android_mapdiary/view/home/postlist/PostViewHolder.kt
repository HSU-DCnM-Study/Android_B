package com.example.android_mapdiary.view.home.postlist

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_mapdiary.R
import com.example.android_mapdiary.databinding.ItemPostBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PostViewHolder(
    private val binding: ItemPostBinding,
    private val onClickUser: (PostItemUiState) -> Unit,
    private val onClickMoreButton: (PostItemUiState) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val storageReference = Firebase.storage.reference

    fun bind(uiState: PostItemUiState) = with(binding) {
        val glide = Glide.with(root)

        glide.load(uiState.writerProfileImageUrl?.let { storageReference.child(it) })
            .fallback(R.drawable.ic_baseline_person_24)
            .into(profileImage)

        userName.text = uiState.writerName
        userName.setOnClickListener {
            onClickUser(uiState)
        }
        profileImage.setOnClickListener {
            onClickUser(uiState)
        }

        moreInfoButton.isVisible = uiState.isMine
        moreInfoButton.setOnClickListener {
            onClickMoreButton(uiState)
        }

        glide.load(storageReference.child(uiState.imageUrl))
            .into(postImage)

        @SuppressLint("SetTextI18n")
        val spannable = SpannableString("${uiState.writerName} ${uiState.content}")
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            uiState.writerName.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        content.text = spannable
        content.isVisible = uiState.content.isNotEmpty()

        timeAgo.text = uiState.timeAgo
    }
}
