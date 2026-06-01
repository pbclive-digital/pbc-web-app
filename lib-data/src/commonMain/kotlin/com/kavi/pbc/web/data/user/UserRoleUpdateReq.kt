package com.kavi.pbc.web.data.user

import kotlinx.serialization.Serializable

@Serializable
data class UserRoleUpdateReq(
    val newRole: String = "",
    val residentMonkFlag: Boolean = false,
    val user: User
)
