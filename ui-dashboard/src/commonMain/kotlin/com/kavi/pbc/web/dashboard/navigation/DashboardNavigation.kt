package com.kavi.pbc.web.dashboard.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.kavi.pbc.web.dashboard.ui.DashboardUI
import com.kavi.pbc.web.dashboard.ui.about.AboutUsUI
import com.kavi.pbc.web.dashboard.ui.contact.ContactUsUI
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import com.kavi.pbc.web.parent.navigation.DashboardPath
import com.kavi.pbc.web.parent.navigation.EventPath
import com.kavi.pbc.web.pbc.container.PBCPageContainer

fun NavGraphBuilder.dashboardNavGraph(navController: NavHostController) {

    // Retrieve User if user signed-in to the application
    var user: User? = null
    ContractServiceLocator.locate(AuthContract::class).retrieveUser(onSuccess = {
        user = it
    }, onFailure = { /* Do nothing */ })

    navigation(startDestination = DashboardPath.DashboardUI.toString(), route = DashboardPath.ROUTE) {
        // Path: dashboard/dashboard-ui
        composable<DashboardPath.DashboardUI> {
            DashboardUI(navController = navController)
        }
        // Path: dashboard/dashboard-ui/<tab-name>
        composable<DashboardPath.DashboardUIWithTab> { backStackEntry ->
            val dashboardArgs = backStackEntry.toRoute<DashboardPath.DashboardUIWithTab>()
            DashboardUI(navController = navController, tabName = dashboardArgs.tabName)
        }
        // Path: dashboard/about-us
        composable<DashboardPath.AboutUs> {
            PBCPageContainer(navController = navController, user = user) {
                AboutUsUI()
            }
        }
        // Path: dashboard/contact-us
        composable<DashboardPath.ContactUs> {
            PBCPageContainer(navController = navController, user = user) {
                ContactUsUI()
            }
        }
    }
}