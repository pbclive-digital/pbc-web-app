package com.kavi.pbc.web.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kavi.pbc.web.app.navigation.AppNavGraph
import com.kavi.pbc.web.common.ui.theme.PBCWebAppTheme
import com.kavi.pbc.web.dashboard.DashboardModule
import com.kavi.pbc.web.event.EventModule
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.NetConfig
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.DashboardContract
import com.kavi.pbc.web.parent.contract.model.EventContract
import com.kavi.pbc.web.parent.contract.model.SplashContract
import com.kavi.pbc.web.splash.SplashModule

@Composable
fun App(
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    // Register UI modules
    registerUiModules()

    // Initiate Network
    Network.shared.initiate(NetConfig(BuildKonfig.API_SCHEME, BuildKonfig.API_DOMAIN))

    val navController = rememberNavController()

    LaunchedEffect(navController) {
        onNavHostReady(navController)
    }

    PBCWebAppTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
        ) {
            AppNavGraph(navController = navController)
        }
    }
}

fun registerUiModules() {
    ContractServiceLocator.register(SplashContract::class) { SplashModule() }
    ContractServiceLocator.register(DashboardContract::class) { DashboardModule() }
    ContractServiceLocator.register(EventContract::class) { EventModule() }
}