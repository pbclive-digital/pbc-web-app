package com.kavi.pbc.web.data.user

enum class UserAuthType(val authType: Int) {
    GOOGLE(101),
    APPLE(102),
    NONE(-100)
}