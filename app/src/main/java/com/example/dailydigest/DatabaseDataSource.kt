package com.example.dailydigest

import com.example.dailydigest.dto.Articles
import kotlinx.coroutines.flow.Flow

interface DatabaseDataSource {
    suspend fun saveNews(tasks: List<Articles>)
    suspend fun getNews(): Flow<List<Articles>>
}