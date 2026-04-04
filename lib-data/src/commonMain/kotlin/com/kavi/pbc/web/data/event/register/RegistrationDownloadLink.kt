package com.kavi.pbc.web.data.event.register

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationDownloadLink(
    val eventId: String,
    val downloadLink: String
)
