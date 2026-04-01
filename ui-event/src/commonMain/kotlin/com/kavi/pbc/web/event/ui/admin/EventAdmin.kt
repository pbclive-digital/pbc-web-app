package com.kavi.pbc.web.event.ui.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.event.data.model.EventManageOrCreate
import com.kavi.pbc.web.event.ui.admin.create.EventCreateUI
import com.kavi.pbc.web.event.ui.admin.manage.EventManageUI

@Composable
fun EventAdminUI(navController: NavController) {
    val eventManageOrCreate = remember { mutableStateOf(EventManageOrCreate.MANAGE) }
    val selectedEventForModify: MutableState<Event?> = remember { mutableStateOf(null) }

    when(eventManageOrCreate.value) {
        EventManageOrCreate.MANAGE -> EventManageUI(
            navController = navController,
            eventManageOrCreate = eventManageOrCreate,
            selectedEventForModify = selectedEventForModify
        )
        EventManageOrCreate.CREATE -> EventCreateUI(
            navController = navController,
            eventManageOrCreate = eventManageOrCreate,
            modifyEvent = selectedEventForModify.value
        )
    }
}