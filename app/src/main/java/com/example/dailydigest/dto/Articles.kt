package com.example.dailydigest.dto

import kotlinx.serialization.Serializable

@Serializable
data class Articles(
    val id: Long? = null,
    val author: String? = "",
    val title: String,
    val description: String? = null,
    val publishedAt: String,
    val source: Source,
    val urlToImage: String? = null,
    val url: String = "",
    val content: String? = null,
)

