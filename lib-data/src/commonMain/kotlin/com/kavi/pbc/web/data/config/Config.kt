package com.kavi.pbc.web.data.config

import com.kavi.pbc.web.data.user.User
import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val dashboardEventCount: Int = 2,
    val dailyQuotesCount: Int = 3,
    val residentMonkList: List<User> = emptyList()
)
