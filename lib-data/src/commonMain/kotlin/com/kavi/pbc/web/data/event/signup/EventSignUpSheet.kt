package com.kavi.pbc.web.data.event.signup

import kotlinx.serialization.Serializable

@Serializable
data class EventSignUpSheet(
    val sheetId: String = "",
    val sheetName: String = "",
    val sheetDescription: String = "",
    val availableCount: Int = 0,
    val allowMultiSignUps: Boolean = false,
    val contributorList: MutableList<EventSignUpSheetContributor> = mutableListOf()
)
