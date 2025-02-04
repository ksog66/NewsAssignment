package com.example.newsassignment.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsassignment.data.local.dao.ArticlesDao
import com.example.newsassignment.data.local.dao.SavedArticlesDao
import com.example.newsassignment.data.local.entity.ArticleEntity
import com.example.newsassignment.data.local.entity.SavedArticleEntity
import com.example.newsassignment.data.util.CustomDateAdapter

@Database(
    entities = [ArticleEntity::class, SavedArticleEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    CustomDateAdapter::class
)
abstract class ArticlesDb : RoomDatabase() {

    abstract fun articlesDao(): ArticlesDao
    abstract fun savedArticlesDao(): SavedArticlesDao

}