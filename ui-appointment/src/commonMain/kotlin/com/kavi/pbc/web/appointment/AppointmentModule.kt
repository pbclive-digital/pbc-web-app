package com.kavi.pbc.web.appointment

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.kavi.pbc.web.appointment.ui.dashboard.AppointmentDashboardUI
import com.kavi.pbc.web.parent.contract.model.AppointmentContract

class AppointmentModule: AppointmentContract {
    @Composable
    override fun GetAppointmentDashboard(navController: NavController) {
        AppointmentDashboardUI(navController = navController)
    }
}