package com.example.newsassignment.presentation.home

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.newsassignment.data.repo.ArticlesRepository
import com.example.newsassignment.data.mapper.toArticle
import com.example.newsassignment.domain.Article
import com.example.newsassignment.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val articlesRepo: ArticlesRepository,
    networkMonitor: NetworkMonitor,
    application: Application
) : AndroidViewModel(application) {

    private var _articlesPager =
        MutableStateFlow<Flow<PagingData<Article>>>(flowOf(PagingData.empty()))
    val articlesPager: StateFlow<Flow<PagingData<Article>>> = _articlesPager

    val defaultCategories =
        mutableStateListOf("Tech", "Politics", "Sports", "Entertainment", "Business")

    var selectedCategory = mutableStateOf(defaultCategories.first())
        private set

    var isSearchPerformedOnce = mutableStateOf(false)
        private set

    val isConnected = networkMonitor
        .isConnected
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

    init {
        try {
            _articlesPager.value = articlesRepo.getPagedArticle(defaultCategories.first())
                .map { pagingData ->
                    pagingData.map { articleEntity ->
                        articleEntity.toArticle()
                    }
                }.cachedIn(viewModelScope)
        } catch (exception: Exception) {
            _articlesPager.value = flow { PagingData.empty<Article>() }
        }
    }

    fun searchArticles(query: String) {
        isSearchPerformedOnce.value = true
        _articlesPager.value = articlesRepo.getPagedArticle(query)
            .map { pagingData ->
                pagingData.map { articleEntity ->
                    articleEntity.toArticle()
                }
            }.cachedIn(viewModelScope)

    }

    fun onCategorySelect(category: String) {
        selectedCategory.value = category
        searchArticles(category)
    }

}