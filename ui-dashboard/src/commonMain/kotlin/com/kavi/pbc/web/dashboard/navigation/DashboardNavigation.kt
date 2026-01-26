package com.kavi.pbc.web.dashboard.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kavi.pbc.web.dashboard.ui.DashboardUI
import com.kavi.pbc.web.parent.navigation.DashboardPath

fun NavGraphBuilder.dashboardNavGraph(navController: NavHostController) {
    navigation(startDestination = DashboardPath.DashboardUI.toString(), route = DashboardPath.ROUTE) {
        // Path: dashboard/dashboard-ui
        composable<DashboardPath.DashboardUI> {
            DashboardUI(navController = navController)
        }
    }
}