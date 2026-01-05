package com.kavi.pbc.web.dashboard

import androidx.compose.runtime.Composable
import com.kavi.pbc.web.dashboard.ui.DashboardUI
import com.kavi.pbc.web.parent.contract.model.DashboardContract

class DashboardModule: DashboardContract {
    @Composable
    override fun RetrieveEntry() {
        DashboardUI()
    }
}