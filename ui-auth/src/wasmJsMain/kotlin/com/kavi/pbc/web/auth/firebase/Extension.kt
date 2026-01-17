package com.kavi.pbc.web.auth.firebase

import com.kavi.pbc.web.data.user.User

fun JsFirebaseUser.toUser(): User {
    return User(
        id = uid,
        email = email!!,
        firstName = displayName,
        lastName = familyName,
        profilePicUrl = photoURL
    )
}