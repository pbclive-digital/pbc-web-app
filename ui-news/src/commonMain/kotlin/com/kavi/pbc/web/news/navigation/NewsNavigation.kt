package com.kavi.pbc.web.news.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.news.ui.list.NewsListUI
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import com.kavi.pbc.web.parent.navigation.NewsPath
import com.kavi.pbc.web.pbc.container.PBCPageContainer

fun NavGraphBuilder.newsNavGraph(navController: NavHostController) {

    // Retrieve User if user signed-in to the application
    var user: User? = null
    ContractServiceLocator.locate(AuthContract::class).retrieveUser(onSuccess = {
        user = it
    }, onFailure = { /* Do nothing */ })

    navigation(startDestination = NewsPath.NewsList.toString(), route = NewsPath.ROUTE) {
        // Path: news/news-list-ui
        composable<NewsPath.NewsList> {
            PBCPageContainer (user = user) {
                NewsListUI(navController = navController)
            }
        }
    }
}