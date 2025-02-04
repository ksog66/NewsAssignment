package com.example.newsassignment.presentation.saved_articles

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newsassignment.R
import com.example.newsassignment.domain.Article
import com.example.newsassignment.presentation.components.ArticleCard
import com.example.newsassignment.ui.theme.Colors.blueBlack
import com.example.newsassignment.ui.theme.Colors.white1

@Composable
fun SavedArticlesRoute(
    modifier: Modifier = Modifier,
    viewModel: SavedArticleViewModel = hiltViewModel(),
    openArticle: (String?) -> Unit
) {
    val articles by viewModel.articles.collectAsStateWithLifecycle()

    SavedArticlesScreen(
        modifier = modifier,
        articles = articles,
        deleteArticle = viewModel::deleteArticle,
        openArticle = openArticle
    )
}

@Composable
fun SavedArticlesScreen(
    modifier: Modifier = Modifier,
    articles: List<Article>,
    deleteArticle: (String) -> Unit,
    openArticle: (String?) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Box(modifier = Modifier.fillMaxWidth().background(blueBlack).padding(horizontal = 24.dp, vertical = 12.dp)) {
                Text(
                    text = stringResource(R.string.app_title),
                    fontSize = 24.sp,
                    color = white1,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }) {
        if (articles.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                items(articles, key = { item -> item.url }) { article ->
                    ArticleCard(
                        modifier = Modifier.fillMaxWidth(),
                        article = article,
                        showDeleteOption = true,
                        onDeleteClick = deleteArticle,
                        onReadMoreClick = openArticle
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 32.dp),
                    painter = painterResource(R.drawable.idle_illustration),
                    contentDescription = "Error Screen"
                )

                Text(
                    modifier = Modifier.padding(top = 32.dp, start = 24.dp, end = 24.dp),
                    text = stringResource(R.string.no_save_articles),
                    style = TextStyle.Default.copy(fontSize = 22.sp),
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}