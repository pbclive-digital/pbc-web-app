package com.kavi.pbc.web.data

import kotlinx.serialization.Serializable

enum class ResponseStatus {
    SUCCESS, ERROR
}

@Serializable
data class Error(
    val message: String
)

@Serializable
data class BaseResponse<T>(
    val status: ResponseStatus,
    val body: T? = null,
    val errors: List<Error>? = emptyList()
)
