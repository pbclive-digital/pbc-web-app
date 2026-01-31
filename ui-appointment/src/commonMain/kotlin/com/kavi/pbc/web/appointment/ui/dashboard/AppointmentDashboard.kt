package com.kavi.pbc.web.appointment.ui.dashboard

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AppointmentDashboardUI(navController: NavController) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxHeight = this.maxHeight
        val maxWidth = this.maxWidth

        Text("Appointments", fontSize = 56.sp)
    }
}