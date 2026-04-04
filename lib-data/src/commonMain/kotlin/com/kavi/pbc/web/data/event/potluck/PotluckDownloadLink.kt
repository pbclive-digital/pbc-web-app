package com.kavi.pbc.web.data.event.potluck

import kotlinx.serialization.Serializable

@Serializable
data class PotluckDownloadLink(
    val eventId: String,
    val downloadLink: String
)
