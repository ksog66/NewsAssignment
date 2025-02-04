package com.example.newsassignment.presentation.saved_articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsassignment.data.local.entity.SavedArticleEntity
import com.example.newsassignment.data.mapper.toArticle
import com.example.newsassignment.data.repo.ArticlesRepository
import com.example.newsassignment.domain.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SavedArticleViewModel @Inject constructor(
    private val articlesRepo: ArticlesRepository
) : ViewModel() {

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()

    init {
        viewModelScope.launch(IO) {
            articlesRepo.fetchSavedArticle().collectLatest {
                _articles.value = it.map(SavedArticleEntity::toArticle)
            }
        }
    }

    fun deleteArticle(url: String) {
        viewModelScope.launch {
            articlesRepo.deleteArticle(url)
        }
    }
}