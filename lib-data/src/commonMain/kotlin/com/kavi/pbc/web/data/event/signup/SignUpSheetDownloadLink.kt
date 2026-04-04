package com.kavi.pbc.web.data.event.signup

import kotlinx.serialization.Serializable

@Serializable
data class SignUpSheetDownloadLink(
    val eventId: String,
    val sheetId: String,
    val downloadLink: String
)
