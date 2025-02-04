package com.example.newsassignment.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.newsassignment.ui.theme.Colors.blueBlack
import com.example.newsassignment.ui.theme.Colors.ltGrey
import com.example.newsassignment.ui.theme.Colors.white1


@Composable
fun AppBottomBar(navController: NavHostController) {
    val items = listOf(
        AppScreens.HomeFeed,
        AppScreens.SavedArticles,
    )
    var selectedItem by remember { mutableIntStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomBarDestination = items.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = white1),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, screen ->
                BottomNavigationItemCustom(
                    screen = screen,
                    isSelected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        navController.navigate(screen.route)
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationItemCustom(screen: AppScreens, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val tint = if (isSelected) blueBlack else ltGrey
        Image(
            imageVector = screen.icon,
            contentDescription = screen.title,
            colorFilter = ColorFilter.tint(tint),
        )
        Text(
            text = screen.title,
            style = TextStyle.Default.copy(color = if (isSelected) blueBlack else ltGrey)
        )
    }
}
