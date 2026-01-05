package com.kavi.pbc.web.data.user

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class User(
    var id: String? = null,
    val email: String,
    var firstName: String? = null,
    var lastName: String? = null,
    var phoneNumber: String? = null,
    var profilePicUrl: String? = null,
    var address: String? = null,
    val userType: UserType = UserType.CONSUMER,
    var residentMonk: Boolean = false,
    var userAuthType: UserAuthType? = UserAuthType.NONE,
)
