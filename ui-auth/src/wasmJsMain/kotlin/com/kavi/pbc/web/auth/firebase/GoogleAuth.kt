package com.kavi.pbc.web.auth.firebase

import kotlin.js.JsName

@JsName("google")
external object Google {
    val accounts: Accounts
}

external interface Accounts {
    val id: Id
}

external interface Id {
    fun initialize(config: GoogleInitConfig)
    fun prompt()
}

external interface GoogleInitConfig {
    var client_id: String
    var callback: (GoogleCredentialResponse) -> Unit
}

external interface GoogleCredentialResponse {
    val credential: String
}