package com.kavi.pbc.web.parent.contract.model

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.kavi.pbc.web.parent.contract.CommonContract

interface NewsContract: CommonContract {

    @Composable
    fun GetNewsList(navController: NavController)
}