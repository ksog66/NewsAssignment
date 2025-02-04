package com.example.newsassignment.data.remote

import com.example.newsassignment.BuildConfig
import com.example.newsassignment.data.remote.QueryValues.SORT_BY_PUBLISHED_AT
import com.example.newsassignment.data.remote.model.ArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {

    @GET("v2/everything")
    suspend fun fetchNewsArticles(
        @Query(Keys.QUERY_KEY) query: String,
        @Query(Keys.SORT_BY_KEY) sortBy: String = SORT_BY_PUBLISHED_AT,
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ): Response<ArticlesResponse>
}