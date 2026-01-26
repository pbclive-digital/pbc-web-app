package com.kavi.pbc.web.parent.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object NewsPath {
    const val ROUTE = "news"

    @Serializable
    @SerialName("${ROUTE}/news-list-ui")
    object NewsList
}