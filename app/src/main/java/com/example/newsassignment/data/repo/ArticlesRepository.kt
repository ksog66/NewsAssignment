package com.example.newsassignment.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.newsassignment.data.local.ArticlesDb
import com.example.newsassignment.data.local.entity.ArticleEntity
import com.example.newsassignment.data.local.entity.SavedArticleEntity
import com.example.newsassignment.data.mapper.toSavedArticleEntity
import com.example.newsassignment.domain.Article
import com.example.newsassignment.domain.DataSource
import com.example.newsassignment.domain.ArticlesRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticlesRepository @Inject constructor(
    private val dataSource: DataSource,
    private val db: ArticlesDb
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getPagedArticle(query: String): Flow<PagingData<ArticleEntity>> {
        val pagingSourceFactory: () -> PagingSource<Int, ArticleEntity> = { db.articlesDao().getPagedArticles()}
        return Pager(
            config = PagingConfig(
                initialLoadSize = 50,
                prefetchDistance = 40,
                pageSize = 20,
                enablePlaceholders = true,
                jumpThreshold = Int.MAX_VALUE
            ),
            remoteMediator = ArticlesRemoteMediator(query, dataSource, db),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun fetchArticleById(id: String) : Flow<ArticleEntity?> {
        return db.articlesDao().fetchArticle(id)
    }

    fun fetchSavedArticle(): Flow<List<SavedArticleEntity>> {
        return db.savedArticlesDao().fetchArticles()
    }
    suspend fun saveArticle(article: Article) {
        db.savedArticlesDao().insertOne(article.toSavedArticleEntity())
    }

    suspend fun deleteArticle(url: String) {
        db.savedArticlesDao().deleteArticle(url)
    }

}
