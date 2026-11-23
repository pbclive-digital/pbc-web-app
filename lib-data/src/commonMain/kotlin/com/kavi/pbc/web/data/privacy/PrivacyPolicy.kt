package com.kavi.pbc.web.data.privacy

import kotlinx.serialization.Serializable

@Serializable
data class PrivacyPolicy(
    val lastUpdated: String,
    val policyItemList: List<PolicyItem>
)
