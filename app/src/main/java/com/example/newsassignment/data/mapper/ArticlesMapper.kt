package com.example.newsassignment.data.mapper

import com.example.newsassignment.data.local.entity.ArticleEntity
import com.example.newsassignment.data.local.entity.SavedArticleEntity
import com.example.newsassignment.data.remote.model.ArticleDto
import com.example.newsassignment.domain.Article

fun ArticleDto.toArticlesEntity(): ArticleEntity {
    return ArticleEntity(
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        source = source,
        title = title,
        url = url,
        urlToImage = urlToImage
    )
}

fun Article.toSavedArticleEntity(): SavedArticleEntity {
    return SavedArticleEntity(
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        source = source,
        title = title,
        url = url,
        urlToImage = urlToImage
    )
}

fun ArticleEntity.toArticle(): Article {
    return Article(
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        source = source,
        title = title,
        url = url,
        urlToImage = urlToImage
    )
}

fun SavedArticleEntity.toArticle(): Article {
    return Article(
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        source = source,
        title = title,
        url = url,
        urlToImage = urlToImage
    )
}