package com.kavi.pbc.web.data.event.register

import kotlinx.serialization.Serializable

@Serializable
data class EventRegistration(
    val id: String,
    val availableSeatCount: Int,
    val registrationList: List<EventRegistrationItem> = emptyList()
)
