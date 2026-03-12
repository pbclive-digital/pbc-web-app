package com.kavi.pbc.web.news

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.kavi.pbc.web.news.ui.list.NewsListUI
import com.kavi.pbc.web.news.ui.manage.NewsManageUI
import com.kavi.pbc.web.parent.contract.model.NewsContract

class NewsModule: NewsContract {

    @Composable
    override fun GetNewsList(navController: NavController) {
        NewsListUI(navController = navController)
    }

    @Composable
    override fun GetNewsManage(navController: NavController) {
        NewsManageUI(navController = navController)
    }
}