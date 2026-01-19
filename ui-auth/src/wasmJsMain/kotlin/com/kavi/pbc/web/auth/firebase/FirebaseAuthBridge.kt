package com.kavi.pbc.web.auth.firebase

import com.kavi.pbc.web.auth.model.JsFirebaseUser

external object FirebaseAuth {
    fun signInWithGoogle(idToken: String)
}

external fun subscribeAuthState(callback: (JsFirebaseUser?) -> Unit): () -> Unit

external fun signOutFirebase()