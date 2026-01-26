package com.kavi.pbc.web.event.ui.selected

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun SelectedEvent(navController: NavController, eventId: String) {
    println("EventID: >>>>>>>>>>>>>>>>>>>>: $eventId")
}