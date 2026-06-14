package com.kavi.pbc.web.data.email.record

import kotlinx.serialization.Serializable

@Serializable
data class EmailRecordContent(
    val subject: String = "",
    val title: String = "",
    val message: String = "",
    val eventDescription: String? = null,
    val eventAgenda: List<String> = emptyList(),
    val eventUrl: String? = null,
)
