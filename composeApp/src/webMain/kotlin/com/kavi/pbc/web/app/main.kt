package com.kavi.pbc.web.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.kavi.pbc.web.dashboard.DashboardModule
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.NetConfig
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.DashboardContract
import com.kavi.pbc.web.parent.contract.model.SplashContract
import com.kavi.pbc.web.splash.SplashModule

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // Register UI modules
    registerUiModules()

    // Initiate Network
    //Network.shared.initiate(NetConfig("https", "pbc-api-staging-1f3fe32cb947.herokuapp.com"))
    Network.shared.initiate(NetConfig("http", "localhost:8082"))

    ComposeViewport {
        App()
    }
}

fun registerUiModules() {
    ContractServiceLocator.register(SplashContract::class) { SplashModule() }
    ContractServiceLocator.register(DashboardContract::class) { DashboardModule() }
}