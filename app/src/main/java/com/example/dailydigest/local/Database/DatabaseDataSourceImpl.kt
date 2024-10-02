package com.example.dailydigest.local.Database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.dailydigest.AppDatabase
import com.example.dailydigest.dto.Articles
import com.example.dailydigest.dto.Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class DatabaseDataSourceImpl(private val appDatabase: AppDatabase) : DatabaseDataSource {
    val queries = appDatabase.appDatabaseQueries
    override suspend fun saveNews(articles: List<Articles>) {
        articles.forEach { article ->
            queries.insertNews(
                author = article.author,
                title = article.title,
                description = article.description,
                publishedAt = article.publishedAt,
                source = article.source.name ?: "",
                sourceId = article.source.id,
                urlToImage = article.urlToImage,
                url = article.url,
                content = article.content,
            )
        }
    }

    override suspend fun getNews(): Flow<List<Articles>> {
        return  queries.selectNews()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { newsEntities ->
                newsEntities.map { article ->
                    Articles(
                        author = article.author,
                        title = article.title,
                        description = article.description,
                        publishedAt = article.publishedAt,
                        urlToImage = article.urlToImage,
                        url = article.url,
                        content = article.content,
                        source = Source(
                            id = article.sourceId,
                            name = article.source
                        )
                    )
                }
            }.flowOn(Dispatchers.Main)
    }

}