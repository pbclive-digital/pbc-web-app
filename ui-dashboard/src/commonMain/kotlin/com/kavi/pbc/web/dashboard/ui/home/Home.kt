package com.kavi.pbc.web.dashboard.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeUI(modifier: Modifier = Modifier) {

    val viewModel: HomeViewModel = viewModel { HomeViewModel() }

    LaunchedEffect(Unit) {
        viewModel.fetchDashboardEvents()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("HOME", fontSize = 56.sp)
    }
}