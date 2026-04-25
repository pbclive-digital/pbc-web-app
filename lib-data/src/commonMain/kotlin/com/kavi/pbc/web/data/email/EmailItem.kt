package com.kavi.pbc.web.data.email

import kotlinx.serialization.Serializable

@Serializable
data class EmailItem(
    val email: String = "",
    val ownerName: String? = null
)
