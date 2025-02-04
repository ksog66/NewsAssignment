package com.example.newsassignment.data.remote

import com.example.newsassignment.core.DispatcherProvider
import com.example.newsassignment.core.Resource
import com.example.newsassignment.data.remote.model.ArticlesResponse
import com.example.newsassignment.domain.DataSource
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val api: NetworkApi
) : DataSource {

    override suspend fun fetchNewsArticles(query: String): Resource<ArticlesResponse> =
        withContext(dispatchers.io) {
            return@withContext try {
                val result = api.fetchNewsArticles(query = query)
                if (result.isSuccessful) {
                    val articles = result.body()
                    Resource.Success(data = articles)
                } else {
                    Resource.Success(null)
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }
    }