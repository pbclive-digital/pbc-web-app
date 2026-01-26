package com.kavi.pbc.web.parent.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object EventPath {
    const val ROUTE = "event"

    @Serializable
    @SerialName("${ROUTE}/event-list-ui")
    object EventList

    @Serializable
    @SerialName("${ROUTE}/event-selected")
    data class EventDetails(
        val eventId: String
    )
}