package com.kavi.pbc.web.data.event.potluck

import kotlinx.serialization.Serializable

@Serializable
data class EventPotluckItem(
    val itemId: String,
    val itemName: String,
    val availableCount: Int,
    val contributorList: MutableList<EventPotluckContributor> = mutableListOf()
) {
    fun completionProgress(): Float {
        return (contributorList.size.toFloat() / availableCount.toFloat())
    }
}
