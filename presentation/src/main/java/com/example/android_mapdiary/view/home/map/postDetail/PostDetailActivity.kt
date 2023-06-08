package com.example.android_mapdiary.view.home.map.postDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.example.android_mapdiary.R
import com.example.android_mapdiary.common.ViewBindingActivity
import com.example.android_mapdiary.databinding.ActivityPostDetailBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PostDetailActivity : ViewBindingActivity<ActivityPostDetailBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityPostDetailBinding
        get() = ActivityPostDetailBinding::inflate

    companion object {
        fun getIntent(
            context: Context,
            writerName: String,
            writerProfileImageUrl: String?,
            content: String,
            imageUrl: String,
            timeAgo: String,
        ): Intent {
            return Intent(context, PostDetailActivity::class.java).apply {
                putExtra("writerName", writerName)
                putExtra("writerProfileImageUrl", writerProfileImageUrl)
                putExtra("content", content)
                putExtra("imageUrl", imageUrl)
                putExtra("timeAgo", timeAgo)
            }
        }
    }

    private fun getWriterName(): String {
        return intent.getStringExtra("writerName")!!
    }

    private fun getWriterProfileImageUrl(): String? {
        return intent.getStringExtra("writerProfileImageUrl")
    }

    private fun getContent(): String {
        return intent.getStringExtra("content")!!
    }

    private fun getImageUrl(): String {
        return intent.getStringExtra("imageUrl")!!
    }

    private fun getTimeAgo(): String {
        return intent.getStringExtra("timeAgo")!!
    }

    private val storageReference = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val glide = Glide.with(this)

        binding.content.text = getContent()
        binding.timeAgo.text = getTimeAgo()
        binding.userName.text = getWriterName()
        glide.load(getWriterProfileImageUrl()?.let { storageReference.child(it) })
            .fallback(R.drawable.ic_baseline_person_24)
            .into(binding.profileImage)

        glide.load(storageReference.child(getImageUrl()))
            .into(binding.postImage)
    }
}