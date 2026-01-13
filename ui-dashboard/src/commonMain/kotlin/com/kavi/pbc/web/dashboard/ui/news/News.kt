package com.kavi.pbc.web.dashboard.ui.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.NewsContract

@Composable
fun NewsUI(modifier: Modifier = Modifier, navController: NavController) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ContractServiceLocator.locate(NewsContract::class).GetNewsList(navController = navController)
    }
}