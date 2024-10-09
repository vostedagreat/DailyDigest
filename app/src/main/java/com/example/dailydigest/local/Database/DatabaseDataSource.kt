package com.example.dailydigest.local.Database

import com.example.dailydigest.dto.Articles
import kotlinx.coroutines.flow.Flow

interface DatabaseDataSource {
    suspend fun saveNews(articles: List<Articles>)
    suspend fun getNews(): Flow<List<Articles>>
    suspend fun getArticle( id : Long): Flow<Articles>

}