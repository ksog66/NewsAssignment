package com.example.newsassignment.presentation.components

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import com.example.newsassignment.ui.theme.Colors.blueBlack
import com.example.newsassignment.ui.theme.Colors.white1


@Composable
fun getSearchBarColor() = TextFieldDefaults.colors(
    focusedContainerColor = white1,
    unfocusedContainerColor = white1,
    focusedIndicatorColor = white1,
    unfocusedIndicatorColor = white1,
    disabledIndicatorColor = white1,
    focusedLabelColor = white1,
    cursorColor = blueBlack
)