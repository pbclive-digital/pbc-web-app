package com.kavi.pbc.web.dashboard.ui.event

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.EventContract

@Composable
fun EventsUI(modifier: Modifier = Modifier, navController: NavController) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ContractServiceLocator.locate(EventContract::class).GetEventListUI(navController = navController)
    }
}