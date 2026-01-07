package com.kavi.pbc.web.dashboard.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kavi.pbc.web.dashboard.ui.DashboardUI
import com.kavi.pbc.web.parent.navigation.DashboardPath

fun NavGraphBuilder.dashboardNavGraph(navController: NavHostController) {
    navigation(startDestination = DashboardPath.DASHBOARD_UI, route = DashboardPath.ROUTE) {
        composable (route = DashboardPath.DASHBOARD_UI) {
            DashboardUI(navController = navController)
        }
    }
}