package com.kavi.pbc.web.data.pagination

import kotlinx.serialization.Serializable

@Serializable
data class PaginationResponse<T>(
    val entityList: MutableList<T>,
    val previousPageLastDocKey: String?
)
