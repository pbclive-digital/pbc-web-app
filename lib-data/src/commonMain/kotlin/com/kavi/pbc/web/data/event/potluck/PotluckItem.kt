package com.kavi.pbc.web.data.event.potluck

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class PotluckItem @OptIn(ExperimentalUuidApi::class) constructor(
    val itemId: String = Uuid.random().toString(),
    val itemName: String,
    val itemCount: Int
)