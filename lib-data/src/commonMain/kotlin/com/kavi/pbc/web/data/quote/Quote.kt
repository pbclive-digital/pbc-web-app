package com.kavi.pbc.web.data.quote

import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    val quote: String,
    val author: String
)