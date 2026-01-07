package com.kavi.pbc.web.data.event.signup

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class SignUpSheet @OptIn(ExperimentalUuidApi::class) constructor(
    val sheetId: String = Uuid.random().toString(),
    val sheetName: String = "",
    val sheetDescription: String = "",
    val availableCount: Int = 0
)