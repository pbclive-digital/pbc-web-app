package com.kavi.pbc.web.parent.contract.model

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.kavi.pbc.web.parent.contract.CommonContract

interface AdminContract: CommonContract {

    @Composable
    fun GetUserManageUI(navController: NavController)

    @Composable
    fun GetEmailGroupManageUI(navController: NavController)

    @Composable
    fun GetBroadcastManageUI(navController: NavController)
}