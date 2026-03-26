package com.kavi.pbc.web.event

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.kavi.pbc.web.event.ui.list.EventListUI
import com.kavi.pbc.web.event.ui.manage.EventManageUI
import com.kavi.pbc.web.parent.contract.model.EventContract

class EventModule: EventContract {
    @Composable
    override fun GetEventListUI(navController: NavController) {
        EventListUI(navController = navController)
    }

    @Composable
    override fun GetEventManageUI(navController: NavController) {
        EventManageUI(navController = navController)
    }

}