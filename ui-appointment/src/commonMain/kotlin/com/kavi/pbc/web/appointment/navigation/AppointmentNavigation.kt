package com.kavi.pbc.web.appointment.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kavi.pbc.web.appointment.ui.dashboard.AppointmentDashboardUI
import com.kavi.pbc.web.parent.navigation.AppointmentPath
import com.kavi.pbc.web.pbc.container.PBCPageContainer

fun NavGraphBuilder.appointmentNavGraph(navController: NavHostController) {
    navigation(startDestination = AppointmentPath.Dashboard.toString(), route = AppointmentPath.ROUTE) {
        // Path: appointment/dashboard-ui
        composable<AppointmentPath.Dashboard> {
            PBCPageContainer {
                AppointmentDashboardUI(navController = navController)
            }
        }
    }
}