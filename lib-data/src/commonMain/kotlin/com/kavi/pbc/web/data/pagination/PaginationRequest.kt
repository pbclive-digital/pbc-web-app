package com.kavi.pbc.web.data.pagination

import kotlinx.serialization.Serializable

@Serializable
data class PaginationRequest(
    var previousPageLastDocKey: String?
)
