package com.example.newsassignment.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsassignment.data.local.entity.SavedArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedArticlesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(articles: SavedArticleEntity)

    @Query("DELETE FROM saved_article WHERE url=:url")
    suspend fun deleteArticle(url: String)

    @Query("SELECT * FROM saved_article")
    fun fetchArticles(): Flow<List<SavedArticleEntity>>
}