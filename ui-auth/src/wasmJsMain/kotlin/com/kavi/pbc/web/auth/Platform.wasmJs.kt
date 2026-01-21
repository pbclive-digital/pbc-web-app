package com.kavi.pbc.web.auth

import androidx.compose.runtime.MutableState
import com.kavi.pbc.web.auth.firebase.FirebaseAuth
import com.kavi.pbc.web.auth.firebase.Google
import com.kavi.pbc.web.auth.firebase.GoogleInitConfig
import com.kavi.pbc.web.auth.firebase.signOutFirebase
import com.kavi.pbc.web.auth.firebase.subscribeAuthState
import com.kavi.pbc.web.auth.model.toUser
import com.kavi.pbc.web.data.user.User

@OptIn(ExperimentalWasmJsInterop::class)
private fun newConfig(): JsAny = js("({})")

actual fun signInWithGoogle() {
    val config = newConfig() as GoogleInitConfig
    config.client_id = "944081515646-5i5uq60q3grjtkmtb0uvufn1102e8ucj.apps.googleusercontent.com"

    config.callback = { response ->
        println("Google token received")
        println("Response: ${response.credential}")
        FirebaseAuth.signInWithGoogle(response.credential)
    }

    Google.accounts.id.initialize(config)

    Google.accounts.id.prompt()
}

/*actual fun retrieveCurrentUser(userState: MutableState<User?>): (() -> Unit)? {
    return subscribeAuthState({ jsUser ->
        userState.value = jsUser?.toUser()
    })
}*/

actual fun retrieveCurrentUser(onUserAvailable: (user: User?) -> Unit) {
    subscribeAuthState { jsUser ->
        onUserAvailable(jsUser?.toUser())
    }
}

actual fun signOutApp() {
    signOutFirebase()
}