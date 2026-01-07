package com.kavi.pbc.web.data.event.potluck

import kotlinx.serialization.Serializable

@Serializable
data class EventPotluck(
    val id: String,
    val potluckItemList: MutableList<EventPotluckItem> = mutableListOf()
)
