package com.kavi.pbc.web.network.session

import com.kavi.pbc.web.data.auth.AuthToken
import com.kavi.pbc.web.data.user.User

object Session {
    var authToken: AuthToken? = null
    var user: User? = null
    //var deviceFactor: DeviceFactor? = DeviceFactor.PHONE

    fun isLogIn(): Boolean {
        return authToken != null && user != null
    }
}