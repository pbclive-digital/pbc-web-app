package com.kavi.pbc.web.parent.contract.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.parent.contract.CommonContract

interface AuthContract: CommonContract {

    fun signInWithFirebaseGoogle()

    fun retrieveCurrentAuthStatus(onComplete: (appAuthStatus: AppAuthStatus) -> Unit)

    fun signOut()

    @Composable
    fun ProvideRegisterUI(showDialog: MutableState<Boolean>, onAuthenticated: () -> Unit,
                          onCreatedWithoutAuth: () -> Unit, onCancel: () -> Unit)

    @Composable
    fun ProvideSignUpInviteUI(showDialog: MutableState<Boolean>, onCancel: () -> Unit)
}