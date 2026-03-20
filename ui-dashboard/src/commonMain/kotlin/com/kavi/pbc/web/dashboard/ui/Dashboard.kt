package com.kavi.pbc.web.dashboard.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kavi.pbc.web.dashboard.ui.admin.AdminUI
import com.kavi.pbc.web.dashboard.ui.event.EventsUI
import com.kavi.pbc.web.dashboard.ui.news.NewsUI
import com.kavi.pbc.web.dashboard.ui.appointment.AppointmentUI
import com.kavi.pbc.web.dashboard.ui.question.QuestionUI
import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.pbc.container.PBCDashboardContainer
import com.kavi.pbc.web.pbc.container.model.TabItem
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_dashboard.generated.resources.Res
import pbcwebapp.ui_dashboard.generated.resources.dashboard_icon_admin
import pbcwebapp.ui_dashboard.generated.resources.dashboard_icon_appointment
import pbcwebapp.ui_dashboard.generated.resources.dashboard_icon_ask_question
import pbcwebapp.ui_dashboard.generated.resources.dashboard_icon_event
import pbcwebapp.ui_dashboard.generated.resources.dashboard_icon_news
import pbcwebapp.ui_dashboard.generated.resources.dashboard_label_admin
import pbcwebapp.ui_dashboard.generated.resources.dashboard_label_appointments
import pbcwebapp.ui_dashboard.generated.resources.dashboard_label_events
import pbcwebapp.ui_dashboard.generated.resources.dashboard_label_news
import pbcwebapp.ui_dashboard.generated.resources.dashboard_label_questions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardUI(navController: NavController) {

    val viewModel: DashboardViewModel = viewModel { DashboardViewModel() }

    val isAdminUser by viewModel.isAdminUser.collectAsState()
    val authTabItemList = remember { mutableStateOf(mutableListOf<TabItem>()) }

    LaunchedEffect(Unit) {
        viewModel.fetchUser()
    }

    authTabItemList.value = mutableListOf(
        /* TabItem(name = "Home", icon = Res.drawable.icon_container_lotus),*/ // TODO - Keep this for future use
        TabItem(name = stringResource(Res.string.dashboard_label_events), icon = Res.drawable.dashboard_icon_event),
        TabItem(name = stringResource(Res.string.dashboard_label_news), icon = Res.drawable.dashboard_icon_news),
        TabItem(name = stringResource(Res.string.dashboard_label_appointments), icon = Res.drawable.dashboard_icon_appointment),
        TabItem(name = stringResource(Res.string.dashboard_label_questions), icon = Res.drawable.dashboard_icon_ask_question),
    )
    if (isAdminUser) {
        authTabItemList.value.add(TabItem(name = stringResource(Res.string.dashboard_label_admin), icon = Res.drawable.dashboard_icon_admin))
    }

    val unAuthTabItemList = listOf(
        /*TabItem(name = "Home", icon = Res.drawable.icon_container_lotus),*/ // TODO - Keep this for future use
        TabItem(name = stringResource(Res.string.dashboard_label_events), icon = Res.drawable.dashboard_icon_event),
        TabItem(name = stringResource(Res.string.dashboard_label_news), icon = Res.drawable.dashboard_icon_news),
        TabItem(name = stringResource(Res.string.dashboard_label_questions), icon = Res.drawable.dashboard_icon_ask_question)
    )

    PBCDashboardContainer (
        authTabItemList = authTabItemList,
        unAuthTabItemList = unAuthTabItemList,
    ) { selectedTabIndex, appAuthStatus ->
        when(appAuthStatus) {
            AppAuthStatus.SIGN_IN -> {
                if (isAdminUser) {
                    when (selectedTabIndex) {
                        /*0 -> HomeUI(navController = navController)*/ // TODO - Keep this for future
                        0 -> EventsUI(navController = navController)
                        1 -> NewsUI(navController = navController)
                        2 -> AppointmentUI(navController = navController)
                        3 -> QuestionUI(navController = navController)
                        4 -> AdminUI(navController = navController)
                    }
                } else {
                    when (selectedTabIndex) {
                        /*0 -> HomeUI(navController = navController)*/ // TODO - Keep this for future
                        0 -> EventsUI(navController = navController)
                        1 -> NewsUI(navController = navController)
                        2 -> AppointmentUI(navController = navController)
                        3 -> QuestionUI(navController = navController)
                    }
                }
            }
            else -> {
                when (selectedTabIndex) {
                    /*0 -> HomeUI(navController = navController)*/ // TODO - Keep this for future
                    0 -> EventsUI(navController = navController)
                    1 -> NewsUI(navController = navController)
                    2 -> QuestionUI(navController = navController)
                }
            }
        }
    }
}