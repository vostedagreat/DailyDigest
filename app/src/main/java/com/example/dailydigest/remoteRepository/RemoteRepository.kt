package com.example.dailydigest.remoteRepository

import com.example.dailydigest.dto.NewsDTO
import domain.models.Results
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
   suspend fun getNews(): Flow<Results<NewsDTO>>
}