package com.example.dailydigest.dto

import kotlinx.serialization.Serializable

@Serializable
data class NewsDTO (
    val status: String,
    val articles: List<Articles>,
)