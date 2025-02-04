package com.example.newsassignment.presentation

import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsassignment.presentation.detail.FullArticleRoute
import com.example.newsassignment.presentation.home.HomeRoute
import com.example.newsassignment.presentation.navigation.ARTICLE_ID_KEY
import com.example.newsassignment.presentation.navigation.AppScreens
import com.example.newsassignment.presentation.navigation.AppBottomBar
import com.example.newsassignment.presentation.navigation.SHOW_SAVE_ARTICLE_KEY
import com.example.newsassignment.presentation.saved_articles.SavedArticlesRoute
import com.example.newsassignment.ui.theme.Colors.white1
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun NewsApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(white1),
        bottomBar = { AppBottomBar(navController) }
    ) { paddingValues ->
        NewsAppNavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            navController = navController
        )
    }
}

@Composable
fun NewsAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = AppScreens.HomeFeed.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(route = AppScreens.HomeFeed.route) {
            HomeRoute(
                modifier = modifier
            ) {
                if (it.isNullOrEmpty()) {
                    Toast.makeText(context, "Invalid Url", Toast.LENGTH_SHORT).show()
                } else {
                    val encodedUrl = URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
                    navController.navigate(
                        AppScreens.FullArticle.passArticleIdAndShowSave(
                            encodedUrl,
                            true
                        )
                    )
                }
            }
        }

        composable(
            route = AppScreens.FullArticle.route,
            arguments = listOf(
                navArgument(ARTICLE_ID_KEY) { type = NavType.StringType },
                navArgument(SHOW_SAVE_ARTICLE_KEY) { type = NavType.BoolType }
            ),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            FullArticleRoute(modifier = modifier) {
                navController.navigateUp()
            }
        }

        composable(
            route = AppScreens.SavedArticles.route
        ) {
            SavedArticlesRoute(modifier = modifier) {
                if (it.isNullOrEmpty()) {
                    Toast.makeText(context, "Invalid Url", Toast.LENGTH_SHORT).show()
                } else {
                    val encodedUrl = URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
                    navController.navigate(
                        AppScreens.FullArticle.passArticleIdAndShowSave(
                            encodedUrl,
                            false
                        )
                    )
                }
            }
        }
    }
}