package com.kavi.pbc.web.data.email

import kotlinx.serialization.Serializable

@Serializable
data class EmailGroup(
    val id: String = "",
    val name: String = "",
    val emails: MutableList<EmailItem> = mutableListOf()
)
