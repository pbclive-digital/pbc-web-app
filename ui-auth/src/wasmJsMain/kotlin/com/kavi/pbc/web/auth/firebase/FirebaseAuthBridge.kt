package com.kavi.pbc.web.auth.firebase

external interface JsFirebaseUser {
    val uid: String
    val email: String?
    val displayName: String?
    val familyName: String?
    val photoURL: String?
}

external object FirebaseAuth {
    fun signInWithGoogle(idToken: String)
}

external fun subscribeAuthState(callback: (JsFirebaseUser?) -> Unit): () -> Unit