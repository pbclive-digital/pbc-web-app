package com.kavi.pbc.web.event.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.kavi.pbc.web.event.ui.list.EventListUI
import com.kavi.pbc.web.event.ui.selected.SelectedEvent
import com.kavi.pbc.web.parent.navigation.EventPath

fun NavGraphBuilder.eventNavGraph(navController: NavHostController) {
    navigation(startDestination = EventPath.EventList.toString(), route = EventPath.ROUTE) {
        // Path: event/event-list-ui
        composable<EventPath.EventList> {
            EventListUI(navController = navController, isContainerRequired = true)
        }
        // Path: event/event-selected/<event-id>
        composable<EventPath.EventDetails> { backStackEntry ->
            val eventArgs = backStackEntry.toRoute<EventPath.EventDetails>()
            SelectedEvent(navController = navController, eventId = eventArgs.eventId)
        }
    }
}