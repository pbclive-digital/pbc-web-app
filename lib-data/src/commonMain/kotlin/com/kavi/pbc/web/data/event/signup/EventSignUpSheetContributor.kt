package com.kavi.pbc.web.data.event.signup

import kotlinx.serialization.Serializable

@Serializable
data class EventSignUpSheetContributor(
    val contributorId: String,
    val contributorName: String,
    val contributorContactNumber: String?
)
