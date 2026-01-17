package com.kavi.pbc.web.auth

import com.kavi.pbc.web.auth.firebase.FirebaseAuth
import com.kavi.pbc.web.auth.firebase.Google
import com.kavi.pbc.web.auth.firebase.GoogleInitConfig

@OptIn(ExperimentalWasmJsInterop::class)
private fun newConfig(): JsAny = js("({})")

actual fun platform(): String {
    return "wasmJsMain"
}

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