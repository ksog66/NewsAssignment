package com.example.newsassignment.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector


const val ARTICLE_ID_KEY = "article_id"
const val SHOW_SAVE_ARTICLE_KEY = "show_article"
sealed class AppScreens(val title:String, val icon:ImageVector, val route: String) {

    data object HomeFeed: AppScreens(title = "Home", icon = Icons.Filled.Home,route = "home_screen")

    data object SavedArticles : AppScreens(title = "Saved Articles",icon = Icons.Filled.Bookmark, route = "saved_feed")

    data object FullArticle: AppScreens(title = "Full Article", icon = Icons.Filled.Fullscreen, route = "full_article?id={$ARTICLE_ID_KEY}&showSaveArticle={$SHOW_SAVE_ARTICLE_KEY}") {
        fun passArticleIdAndShowSave(
            id: String,
            showSave: Boolean
        ): String {
            return "full_article?id=$id&showSaveArticle=$showSave"
        }
    }
}