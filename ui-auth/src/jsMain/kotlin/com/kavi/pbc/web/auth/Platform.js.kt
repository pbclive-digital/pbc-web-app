package com.kavi.pbc.web.auth

import androidx.compose.runtime.MutableState
import com.kavi.pbc.web.data.user.User

actual fun platform(): String {
    return "JsMain"
}

actual fun signInWithGoogle() {
}

actual fun retrieveCurrentUser(userState: MutableState<User?>): (() -> Unit)? {
    return null
}

actual fun retrieveCurrentUser(onUserAvailable: (user: User?) -> Unit) {
}