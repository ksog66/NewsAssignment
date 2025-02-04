package com.example.newsassignment.domain

import com.example.newsassignment.core.Resource
import com.example.newsassignment.data.remote.model.ArticlesResponse

interface DataSource {
    suspend fun fetchNewsArticles(query: String): Resource<ArticlesResponse>
}