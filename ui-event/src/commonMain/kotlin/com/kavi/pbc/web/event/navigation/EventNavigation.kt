package com.kavi.pbc.web.event.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kavi.pbc.web.event.ui.list.EventListUI
import com.kavi.pbc.web.parent.navigation.EventPath

fun NavGraphBuilder.eventNavGraph(navController: NavHostController) {
    navigation(startDestination = EventPath.EVENT_LIST_UI, route = EventPath.ROUTE) {
        composable (route = EventPath.EVENT_LIST_UI) {
            EventListUI(navController = navController)
        }
    }
}