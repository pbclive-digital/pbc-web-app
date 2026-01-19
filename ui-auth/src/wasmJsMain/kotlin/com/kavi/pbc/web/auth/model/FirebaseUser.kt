package com.kavi.pbc.web.auth.model

import com.kavi.pbc.web.data.user.User

external interface JsFirebaseUser {
    val uid: String
    val email: String?
    val displayName: String?
    val familyName: String?
    val photoURL: String?
}

fun JsFirebaseUser.toUser(): User {
    return User(
        id = uid,
        email = email!!,
        firstName = displayName,
        lastName = familyName,
        profilePicUrl = photoURL
    )
}