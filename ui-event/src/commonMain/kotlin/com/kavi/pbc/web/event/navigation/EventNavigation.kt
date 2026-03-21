package com.kavi.pbc.web.event.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.event.ui.list.EventListUI
import com.kavi.pbc.web.event.ui.selected.SelectedEvent
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import com.kavi.pbc.web.parent.navigation.EventPath
import com.kavi.pbc.web.pbc.container.PBCPageContainer

fun NavGraphBuilder.eventNavGraph(navController: NavHostController) {

    // Retrieve User if user signed-in to the application
    var user: User? = null
    ContractServiceLocator.locate(AuthContract::class).retrieveUser(onSuccess = {
        user = it
    }, onFailure = { /* Do nothing */ })

    navigation(startDestination = EventPath.EventList.toString(), route = EventPath.ROUTE) {
        // Path: event/event-list-ui
        composable<EventPath.EventList> {
            PBCPageContainer (user = user) {
                EventListUI(navController = navController)
            }
        }
        // Path: event/event-selected/<event-id>
        composable<EventPath.EventDetails> { backStackEntry ->
            val eventArgs = backStackEntry.toRoute<EventPath.EventDetails>()
            PBCPageContainer (user = user) {
                SelectedEvent(navController = navController, eventId = eventArgs.eventId)
            }
        }
    }
}