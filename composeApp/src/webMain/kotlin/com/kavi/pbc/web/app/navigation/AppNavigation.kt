package com.kavi.pbc.web.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kavi.pbc.web.dashboard.navigation.dashboardNavGraph
import com.kavi.pbc.web.event.navigation.eventNavGraph
import com.kavi.pbc.web.news.navigation.newsNavGraph
import com.kavi.pbc.web.parent.navigation.SplashPath
import com.kavi.pbc.web.splash.navigation.splashNavGraph

@Composable
fun AppNavGraph(navController: NavHostController) {
    // Navigation start from splash module
    NavHost(navController = navController, startDestination = SplashPath.ROUTE) {
        // Include splash nav-graph
        splashNavGraph(navController = navController)
        // Include dashboard nav-graph
        dashboardNavGraph(navController = navController)
        // Include event nav-graph
        eventNavGraph(navController = navController)
        // Include news nav-graph
        newsNavGraph(navController = navController)
    }
}