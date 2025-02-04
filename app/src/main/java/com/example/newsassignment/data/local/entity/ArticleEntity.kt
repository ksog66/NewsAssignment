package com.example.newsassignment.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsassignment.data.remote.model.Source
import java.util.Date

@Entity("article")
data class ArticleEntity(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: Date?,
    @Embedded(prefix = "source_")
    val source: Source?,
    val title: String?,
    @PrimaryKey
    val url: String,
    val urlToImage: String?
)

@Entity("saved_article")
data class SavedArticleEntity(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: Date?,
    @Embedded(prefix = "source_")
    val source: Source?,
    val title: String?,
    @PrimaryKey
    val url: String,
    val urlToImage: String?
)