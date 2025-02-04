package com.example.newsassignment.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newsassignment.ui.theme.Colors.blueBlack
import com.example.newsassignment.ui.theme.Colors.ltGrey
import com.example.newsassignment.ui.theme.Colors.white1


@Composable
fun CategoryRow(
    modifier: Modifier = Modifier,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier
            .background(color = white1)
            .horizontalScroll(scrollState)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            val tabColor = if (isSelected) blueBlack else ltGrey
            val textColor = if (isSelected) white1 else blueBlack

            Box(
                modifier = Modifier
                    .background(tabColor, shape = RoundedCornerShape(8.dp))
                    .clickable { onCategorySelect(category) }
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Text(text = category, color = textColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}