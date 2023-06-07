package com.example.data.model

import java.util.*

data class PostDto(
    val uuid: String = "",
    val writerUuid: String = "",
    val content: String = "",
    val dateTime: Date = Date(),
    val imageUrl: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)
