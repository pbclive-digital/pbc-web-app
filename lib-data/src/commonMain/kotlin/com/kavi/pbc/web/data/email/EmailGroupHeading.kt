package com.kavi.pbc.web.data.email

import kotlinx.serialization.Serializable

@Serializable
data class EmailGroupHeading(
    val id: String = "",
    val name: String = "",
)
