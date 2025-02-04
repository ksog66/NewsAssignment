package com.example.newsassignment.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.newsassignment.R
import com.example.newsassignment.domain.Article
import com.example.newsassignment.ui.theme.Colors.white1

@Composable
fun ArticleCard(
    modifier: Modifier = Modifier,
    article: Article,
    showDeleteOption: Boolean = false,
    onDeleteClick: (String) -> Unit = {},
    onReadMoreClick:(String?) -> Unit
) {
    Card(
        modifier = modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = white1)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = article.urlToImage,
                    contentDescription = "Article poster ${article.title}",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(white1),
                    contentScale = ContentScale.FillBounds
                )
                HorizontalSpace(8.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = article.title ?: "News title", maxLines = 1, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    VerticalSpace(4.dp)
                    article.description?.let {
                        Text(text = it, maxLines = 3, fontSize = 11.sp, fontWeight = FontWeight.Normal)
                    }
                }
                if (showDeleteOption) {
                    HorizontalSpace(2.dp)
                    IconButton(onClick = {onDeleteClick.invoke(article.url)}) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete Article")
                    }
                }
            }
            Text(modifier = Modifier.align(Alignment.End).clickable { onReadMoreClick.invoke(article.url) }, text = stringResource(R.string.read_more), textDecoration = TextDecoration.Underline)
        }
    }
}
