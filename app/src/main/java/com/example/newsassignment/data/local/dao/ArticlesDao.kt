package com.example.newsassignment.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsassignment.data.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM article ORDER BY publishedAt DESC")
    fun getPagedArticles(): PagingSource<Int, ArticleEntity>

    @Query("SELECT * FROM article WHERE url=:url")
    fun fetchArticle(url: String): Flow<ArticleEntity?>

    @Query("DELETE FROM article")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ArticleEntity>)
}