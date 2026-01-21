package com.kavi.pbc.web.auth.model

import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.data.user.UserAuthType

external interface JsFirebaseUser {
    val uid: String
    val email: String?
    val displayName: String?
    val phoneNumber: String?
    val photoURL: String?
}

fun JsFirebaseUser.toUser(): User {
    val user = User(email = email!!)

    user.id = uid
    val displayNames = displayName?.split(" ")
    displayNames?.let {
        user.lastName = it.last()
        user.firstName = it.first()
    }
    user.userAuthType = UserAuthType.GOOGLE
    user.phoneNumber = phoneNumber
    user.profilePicUrl = photoURL

    return user
}