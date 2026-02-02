package com.kavi.pbc.web.auth.firebase

import com.kavi.pbc.web.auth.model.JsFirebaseUser
import kotlin.js.Promise

@OptIn(ExperimentalWasmJsInterop::class)
external fun signInWithGoogle(): Promise<JsString>

external fun subscribeAuthState(callback: (JsFirebaseUser?) -> Unit): () -> Unit

external fun signOutFirebase()