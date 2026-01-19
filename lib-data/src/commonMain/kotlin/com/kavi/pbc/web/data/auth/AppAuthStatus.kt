package com.kavi.pbc.web.data.auth

import kotlinx.serialization.Serializable

@Serializable
enum class AppAuthStatus {
    NONE, SIGN_IN, SIGN_UP_REQUIRED, FAILED
}