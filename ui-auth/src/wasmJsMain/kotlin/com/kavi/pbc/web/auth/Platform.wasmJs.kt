package com.kavi.pbc.web.auth

import com.kavi.pbc.web.auth.firebase.signInWithGoogle
import com.kavi.pbc.web.auth.firebase.signOutFirebase
import com.kavi.pbc.web.auth.firebase.subscribeAuthState
import com.kavi.pbc.web.auth.model.toUser
import com.kavi.pbc.web.data.user.User

@OptIn(ExperimentalWasmJsInterop::class)
actual fun signInWithGoogle() {
    signInWithGoogle()
}

actual fun retrieveCurrentUser(onUserAvailable: (user: User?) -> Unit) {
    subscribeAuthState { jsUser ->
        onUserAvailable(jsUser?.toUser())
    }
}

actual fun signOutApp() {
    signOutFirebase()
}