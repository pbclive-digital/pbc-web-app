package com.kavi.pbc.web.appointment.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kavi.pbc.web.appointment.ui.dashboard.AppointmentDashboardUI
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import com.kavi.pbc.web.parent.navigation.AppointmentPath
import com.kavi.pbc.web.pbc.container.PBCPageContainer

fun NavGraphBuilder.appointmentNavGraph(navController: NavHostController) {

    // Retrieve User if user signed-in to the application
    var user: User? = null
    ContractServiceLocator.locate(AuthContract::class).retrieveUser(onSuccess = {
        user = it
    }, onFailure = { /* Do nothing */ })

    navigation(startDestination = AppointmentPath.Dashboard.toString(), route = AppointmentPath.ROUTE) {
        // Path: appointment/dashboard-ui
        composable<AppointmentPath.Dashboard> {
            PBCPageContainer (user = user, navController = navController) {
                AppointmentDashboardUI(navController = navController)
            }
        }
    }
}