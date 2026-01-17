package com.kavi.pbc.web.auth.auth

import com.kavi.pbc.web.auth.firebase.FirebaseAuth
import com.kavi.pbc.web.auth.firebase.Google
import com.kavi.pbc.web.auth.firebase.GoogleInitConfig
import com.kavi.pbc.web.parent.contract.model.AuthContract

@OptIn(ExperimentalWasmJsInterop::class)
private fun newConfig(): JsAny = js("({})")

class AuthModule: AuthContract {
    override fun signInWithFirebaseGoogle() {

    }
}