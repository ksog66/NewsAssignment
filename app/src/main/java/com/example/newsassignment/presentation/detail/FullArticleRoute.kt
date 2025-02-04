package com.example.newsassignment.presentation.detail

import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.newsassignment.R
import com.example.newsassignment.domain.Article
import com.example.newsassignment.presentation.components.GenericErrorScreen
import com.example.newsassignment.presentation.components.HorizontalSpace
import com.example.newsassignment.presentation.components.LoadingScreen
import com.example.newsassignment.ui.theme.Colors.blueBlack
import com.example.newsassignment.ui.theme.Colors.white1


@Composable
fun FullArticleRoute(
    modifier: Modifier = Modifier,
    viewModel: ArticleViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val showSaveButton = viewModel.showSaveButton

    val message by viewModel.message

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    when (uiState) {
        is ArticleUiState.Error -> {
            val errorMessage = (uiState as ArticleUiState.Error).message
            GenericErrorScreen(
                modifier = modifier,
                message = errorMessage,
                onBackClick = navigateBack,
                ctaText = stringResource(R.string.retry),
                onCtaClick = viewModel::fetchArticle
            )
        }

        is ArticleUiState.Loading -> {
            LoadingScreen(modifier, stringResource(R.string.loading_news_for_you))
        }

        is ArticleUiState.Success -> {
            val article = (uiState as ArticleUiState.Success).article
            FullArticleScreen(
                modifier = modifier,
                article = article,
                showSaveButton = showSaveButton,
                navigateBack = navigateBack,
                saveArticle = viewModel::saveArticle
            )
        }
    }

}

@Composable
fun FullArticleScreen(
    modifier: Modifier = Modifier,
    article: Article,
    showSaveButton: Boolean,
    navigateBack: () -> Unit,
    saveArticle: (Article) -> Unit
) {
    val fabText = stringResource(R.string.save)

    Scaffold(
        modifier = modifier,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(blueBlack)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(modifier = Modifier.size(24.dp), onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Navigate Back",
                        tint = white1
                    )
                }

                HorizontalSpace(16.dp)

                Text(
                    text = stringResource(R.string.app_title),
                    fontSize = 24.sp,
                    color = white1,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        floatingActionButton = {
            if (showSaveButton) {
                ExtendedFloatingActionButton(
                    onClick = { saveArticle.invoke(article) }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = fabText)
                        HorizontalSpace(8.dp)
                        Icon(
                            imageVector = Icons.Filled.Bookmark,
                            contentDescription = "Save Article"
                        )
                    }
                }
            }
        }
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            }, update = {
                it.loadUrl(article.url)
            }
        )
    }
}