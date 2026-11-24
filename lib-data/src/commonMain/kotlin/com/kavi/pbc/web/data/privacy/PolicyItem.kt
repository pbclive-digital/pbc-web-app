package com.kavi.pbc.web.data.privacy

import kotlinx.serialization.Serializable

@Serializable
data class PolicyItem(
    val id: String,
    val title: String,
    val description: String,
    val secondDescription: String?,
    val pointList: List<String> = emptyList()
)
