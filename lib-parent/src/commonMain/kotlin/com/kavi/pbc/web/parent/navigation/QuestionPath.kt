package com.kavi.pbc.web.parent.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object QuestionPath {
    const val ROUTE = "question"

    @Serializable
    @SerialName("${ROUTE}/question-list-ui")
    object QuestionList
}