package com.kavi.pbc.web.news.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kavi.pbc.web.common.ui.component.PageContainer
import com.kavi.pbc.web.news.ui.list.NewsListUI
import com.kavi.pbc.web.parent.navigation.NewsPath

fun NavGraphBuilder.newsNavGraph(navController: NavHostController) {
    navigation(startDestination = NewsPath.NewsList.toString(), route = NewsPath.ROUTE) {
        // Path: news/news-list-ui
        composable<NewsPath.NewsList> {
            PageContainer {
                NewsListUI(navController = navController)
            }
        }
    }
}