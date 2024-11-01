package com.example.dailydigest.local.Database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.example.dailydigest.AppDatabase
import com.example.dailydigest.dto.Articles
import com.example.dailydigest.dto.Source
import com.example.dailydigest.models.toArticle
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
                   article.toArticle()
                }
            }.flowOn(Dispatchers.Main)
    }

    override suspend fun getArticle(id: Long): Flow<Articles> {
        println("article is: $id")
        return queries.selectArticle(id)
            .asFlow()
            .mapToOne(Dispatchers.IO)
            .map{ article->
                article.toArticle()
            }
    }


    override suspend fun searchNews(searchTerm: String): List<Articles> {
        val articles = queries.searchArticle(searchTerm).executeAsList()
        return  articles.map { article->
            article.toArticle()
        }
    }
}