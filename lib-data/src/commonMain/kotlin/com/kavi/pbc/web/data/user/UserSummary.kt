package com.kavi.pbc.web.data.user

import kotlinx.serialization.Serializable

@Serializable
data class UserSummary(
    val id: String = "",
    val name: String = "",
    val imageUrl: String? = null
)
