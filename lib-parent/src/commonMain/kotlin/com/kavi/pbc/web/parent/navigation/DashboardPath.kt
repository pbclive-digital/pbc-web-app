package com.kavi.pbc.web.parent.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object DashboardPath {
    const val ROUTE = "dashboard"

    @Serializable
    @SerialName("${ROUTE}/dashboard-ui")
    object DashboardUI
}