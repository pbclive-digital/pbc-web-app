package com.kavi.pbc.web.parent.contract.model

import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.parent.contract.CommonContract

interface AuthContract: CommonContract {

    fun signInWithFirebaseGoogle()

    fun retrieveCurrentAuthStatus(onComplete: (appAuthStatus: AppAuthStatus) -> Unit)
}