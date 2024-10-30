package com.example.dailydigest.models

import com.example.dailydigest.dto.Articles
import com.example.dailydigest.dto.Source
import database.NewsEntity

fun NewsEntity.toArticle(): Articles{
    return Articles(
        id = id,
        author = author,
        title = title,
        description = description,
        publishedAt = publishedAt,
        urlToImage = urlToImage,
        url = url,
        content = content,
        source = Source(
            id = sourceId,
            name = source
        )
    )
}