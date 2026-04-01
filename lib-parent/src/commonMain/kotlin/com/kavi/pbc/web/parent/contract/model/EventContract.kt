package com.kavi.pbc.web.parent.contract.model

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.kavi.pbc.web.parent.contract.CommonContract

interface EventContract: CommonContract {

    @Composable
    fun GetEventListUI(navController: NavController)

    @Composable
    fun GetEventAdminUI(navController: NavController)
}