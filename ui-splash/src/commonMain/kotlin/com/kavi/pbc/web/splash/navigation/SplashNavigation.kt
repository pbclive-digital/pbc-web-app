package com.kavi.pbc.web.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kavi.pbc.web.parent.navigation.SplashPath
import com.kavi.pbc.web.splash.ui.SplashUI

// IMPORT: Kept this in older way of nav graph due to an issue on navigation start with newer way
// of nav graph in other modules.
fun NavGraphBuilder.splashNavGraph(navController: NavHostController) {
    navigation(startDestination = SplashPath.SPLASH_UI, route = SplashPath.ROUTE) {
        composable (route = SplashPath.SPLASH_UI) {
            SplashUI(navController = navController)
        }
    }
}