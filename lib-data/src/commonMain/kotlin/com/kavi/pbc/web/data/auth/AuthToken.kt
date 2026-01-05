package com.kavi.pbc.web.data.auth

import kotlinx.serialization.Serializable

@Serializable
enum class TokenStatus(val status: String) {
    VALID("VALID"),
    EXPIRED("EXPIRED"),
    SUSPENDED("SUSPENDED"),
    REMOVED("REMOVED")
}

@Serializable
data class AuthToken(
    val id: String? = null,
    val email: String,
    val userId: String? = null,
    var token: String? = null,
    val status: TokenStatus,
    val createdTime: Long? = null
)