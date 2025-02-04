package com.example.newsassignment.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.newsassignment.R
import com.example.newsassignment.domain.Article
import com.example.newsassignment.presentation.components.CategoryRow
import com.example.newsassignment.presentation.components.ArticleCard
import com.example.newsassignment.presentation.components.GenericErrorScreen
import com.example.newsassignment.presentation.components.LoadingScreen
import com.example.newsassignment.presentation.components.VerticalSpace
import com.example.newsassignment.presentation.components.getSearchBarColor
import com.example.newsassignment.ui.theme.Colors.blueBlack
import com.example.newsassignment.ui.theme.Colors.dangerRed
import com.example.newsassignment.ui.theme.Colors.white1
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    openArticle: (String?) -> Unit
) {
    val articlesLazyList = viewModel.articlesPager.collectAsState().value.collectAsLazyPagingItems()
    val defaultCategories = viewModel.defaultCategories
    val selectedCategory by viewModel.selectedCategory

    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()

    val isSearchPerformedOnce by viewModel.isSearchPerformedOnce

    val (searchQuery, onQueryChange) = remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        snapshotFlow { searchQuery }
            .debounce(500)
            .distinctUntilChanged()
            .collect {
                if (it.isNotEmpty()) {
                    viewModel.searchArticles(it)
                } else {
                    if (isSearchPerformedOnce) {
                        viewModel.searchArticles(selectedCategory)
                    }
                }
            }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white1)
    ) {
        if (articlesLazyList.itemSnapshotList.isEmpty() && articlesLazyList.loadState.refresh is LoadState.Error) {
            val errorMessage = (articlesLazyList.loadState.refresh as LoadState.Error).error.message
            GenericErrorScreen(
                modifier = modifier,
                message = errorMessage,
                ctaText = stringResource(R.string.retry),
                onCtaClick = {
                    articlesLazyList.refresh()
                }
            )
        } else {
            HomeScreen(
                modifier = modifier,
                searchQuery = searchQuery,
                articles = articlesLazyList,
                isConnected = isConnected,
                defaultCategories = defaultCategories,
                selectedCategory = selectedCategory,
                onQueryChange = onQueryChange,
                onCategorySelect = viewModel::onCategorySelect,
                openArticle = openArticle
            )
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    searchQuery: String,
    isConnected: Boolean,
    onQueryChange: (String) -> Unit,
    articles: LazyPagingItems<Article>,
    defaultCategories: List<String>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    openArticle: (String?) -> Unit
) {
    val lazyListState = rememberLazyListState()
    Scaffold(
        modifier = modifier.background(white1),
        topBar = {
            HomeAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                isConnected = isConnected,
                searchQuery = searchQuery,
                onQueryChange = onQueryChange
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(white1)
                .padding(it)
        ) {
            VerticalSpace(16.dp)
            if (isConnected) {
                if (searchQuery.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = stringResource(R.string.showing_result_for, searchQuery),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = "Categories",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CategoryRow(
                        modifier = Modifier.fillMaxWidth(),
                        categories = defaultCategories,
                        selectedCategory = selectedCategory,
                        onCategorySelect = onCategorySelect
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.showing_cached_results),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            VerticalSpace(16.dp)
            if (articles.loadState.refresh is LoadState.Loading) {
                LoadingScreen(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(white1),
                    message = stringResource(R.string.loading_news_for_you)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(white1),
                    state = lazyListState
                ) {
                    items(
                        count = articles.itemCount,
                        key = articles.itemKey { article -> article.url },
                        contentType = articles.itemContentType {
                            "News Articles"
                        }
                    ) { index: Int ->
                        val feedItem = articles[index]

                        if (feedItem != null) {
                            ArticleCard(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                article = feedItem,
                                onReadMoreClick = openArticle
                            )
                        }
                        if (articles.loadState.append is LoadState.Error) {
                            val e = articles.loadState.append as LoadState.Error
                            val message = e.error.message
                            ErrorLog(text = message)
                        }
                    }
                }
            }

            if (articles.loadState.append == LoadState.Loading) {
                LoadingItem()
            }
        }
    }
}

@Composable
fun HomeAppBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    isConnected: Boolean,
    onQueryChange: (String) -> Unit
) {
    Column(
        modifier = modifier
            .background(color = blueBlack)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.app_title),
            fontSize = 24.sp,
            color = white1,
            fontWeight = FontWeight.SemiBold
        )

        if (isConnected) {
            VerticalSpace(12.dp)
            OutlinedTextField(
                shape = RoundedCornerShape(24.dp),
                value = searchQuery,
                onValueChange = onQueryChange,
                label = { Text(stringResource(R.string.search)) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp)),
                colors = getSearchBarColor()
            )
        } else {
            Text(
                text = stringResource(R.string.no_internet_connection),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        VerticalSpace(12.dp)
    }
}

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(42.dp)
                .height(42.dp)
                .padding(8.dp),
            strokeWidth = 5.dp
        )

    }
}

@Composable
fun ErrorLog(
    modifier: Modifier = Modifier,
    text: String?
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(shape = RoundedCornerShape(8.dp), color = dangerRed)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_error),
            contentDescription = "Error fetching news"
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            text = text ?: stringResource(R.string.generic_error_message),
            style = TextStyle.Default.copy(fontSize = 14.sp, color = white1),
        )
    }

}


