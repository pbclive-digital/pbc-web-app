package com.kavi.pbc.web.appointment.ui.dashboard

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil

@Composable
fun AppointmentDashboardUI(navController: NavController) {

    val viewModel: AppointmentDashboardViewModel = viewModel { AppointmentDashboardViewModel() }

    LaunchedEffect(Unit) {
        viewModel.checkAppointmentReqCreateEligibility()
        viewModel.fetchResidentMonkList()
        viewModel.fetchAppointmentRequests()
        viewModel.fetchAppointments()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxHeight = this.maxHeight
        val maxWidth = this.maxWidth

        when(UIUtil.screenType(maxWidth = maxWidth)) {
            ScreenType.PHONE -> MobileContent(screenWidth = maxWidth, screenHeight = maxHeight, viewModel = viewModel)
            else -> WebContent(maxHeight = maxHeight, viewModel = viewModel)
        }
    }
}