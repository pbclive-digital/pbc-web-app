package com.kavi.pbc.web.parent.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object AppointmentPath {
    const val ROUTE = "appointment"

    @Serializable
    @SerialName("${ROUTE}/dashboard-ui")
    object Dashboard

}