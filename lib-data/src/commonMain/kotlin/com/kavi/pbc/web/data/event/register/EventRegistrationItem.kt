package com.kavi.pbc.web.data.event.register

import kotlinx.serialization.Serializable

@Serializable
data class EventRegistrationItem(
    val participantUserId: String,
    val participantName: String,
    val participantContactNumber: String? = null,
    val participantAddress: String? = null
)
