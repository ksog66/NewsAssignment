package com.example.newsassignment.domain

import com.example.newsassignment.data.remote.model.Source
import java.util.Date

data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: Date?,
    val source: Source?,
    val title: String?,
    val url: String,
    val urlToImage: String?
)