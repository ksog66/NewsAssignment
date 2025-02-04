package com.example.newsassignment.presentation.detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsassignment.data.mapper.toArticle
import com.example.newsassignment.data.repo.ArticlesRepository
import com.example.newsassignment.domain.Article
import com.example.newsassignment.presentation.navigation.ARTICLE_ID_KEY
import com.example.newsassignment.presentation.navigation.SHOW_SAVE_ARTICLE_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articlesRepo: ArticlesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val articleId: String = savedStateHandle[ARTICLE_ID_KEY] ?: ""
    val showSaveButton: Boolean = savedStateHandle[SHOW_SAVE_ARTICLE_KEY] ?: true

    private val _uiState = MutableStateFlow<ArticleUiState>(ArticleUiState.Loading)
    val uiState: StateFlow<ArticleUiState> = _uiState

    var message = mutableStateOf<String?>(null)

    init {
        fetchArticle()
    }

    fun fetchArticle() {
        viewModelScope.launch {
            _uiState.value = ArticleUiState.Loading
            try {
                articlesRepo.fetchArticleById(articleId).collectLatest {articleEntity ->
                    if (articleEntity != null) {
                        _uiState.value = ArticleUiState.Success(articleEntity.toArticle())
                    } else {
                        _uiState.value = ArticleUiState.Error("Couldn't find the article")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ArticleUiState.Error(e.message)
            }
        }
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            try {
                articlesRepo.saveArticle(article)
                message.value = "Article Saved Successfully"
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }
}

sealed interface ArticleUiState {
    data object Loading: ArticleUiState

    data class Error(val message: String?): ArticleUiState

    data class Success(val article: Article): ArticleUiState

}