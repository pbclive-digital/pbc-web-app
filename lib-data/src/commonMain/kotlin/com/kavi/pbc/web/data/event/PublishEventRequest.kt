package com.kavi.pbc.web.data.event

import com.kavi.pbc.web.data.email.EmailGroupHeading
import com.kavi.pbc.web.data.event.Event
import kotlinx.serialization.Serializable

@Serializable
data class PublishEventRequest(
    val event: Event,
    val emailGroupHeadings: List<EmailGroupHeading>
)
