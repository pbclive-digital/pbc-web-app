package com.kavi.pbc.web.auth

import com.kavi.pbc.web.parent.contract.model.AuthContract

class AuthModule: AuthContract {
    override fun signInWithFirebaseGoogle() {
        signInWithGoogle()
    }
}