package com.example.newsassignment.domain

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.newsassignment.core.Resource
import com.example.newsassignment.data.local.ArticlesDb
import com.example.newsassignment.data.local.entity.ArticleEntity
import com.example.newsassignment.data.mapper.toArticlesEntity
import com.example.newsassignment.data.remote.model.ArticleDto

@OptIn(ExperimentalPagingApi::class)
class ArticlesRemoteMediator(
    private val query: String,
    private val dataSource: DataSource,
    private val db: ArticlesDb
) : RemoteMediator<Int, ArticleEntity>() {

    private var hasMoreData = true
    private var hasNetworkDataLoaded = false

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        return try {
            if (!hasMoreData) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            if (loadType == LoadType.PREPEND) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            if (loadType == LoadType.REFRESH) {
                hasNetworkDataLoaded = false
            }

            if (!hasNetworkDataLoaded) {
                when (val apiResponse = dataSource.fetchNewsArticles(query)) {
                    is Resource.Error -> {
                        return MediatorResult.Error(apiResponse.e)
                    }
                    is Resource.Success -> {
                         val articles = apiResponse.data?.articles?.map(ArticleDto::toArticlesEntity) ?: emptyList()

                        db.withTransaction {
                            if (loadType == LoadType.REFRESH) {
                                db.articlesDao().clearAll()
                            }
                            db.articlesDao().insertAll(articles)
                        }

                        hasMoreData = articles.isNotEmpty()
                        hasNetworkDataLoaded = articles.isNotEmpty()
                        return MediatorResult.Success(endOfPaginationReached = !hasMoreData)
                    }
                    is Resource.Loading -> {
                        return MediatorResult.Success(endOfPaginationReached = false)
                    }
                }
            } else {
                return MediatorResult.Success(endOfPaginationReached = !hasMoreData)
            }
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }
}
