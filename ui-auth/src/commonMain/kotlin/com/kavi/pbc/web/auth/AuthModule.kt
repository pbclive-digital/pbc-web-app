package com.kavi.pbc.web.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.kavi.pbc.web.auth.service.auth.AuthServiceModel
import com.kavi.pbc.web.auth.ui.invite.SignUpInviteDialog
import com.kavi.pbc.web.auth.ui.register.RegisterDialog
import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.datastore.AppLocalStore
import com.kavi.pbc.web.datastore.DataKey
import com.kavi.pbc.web.parent.contract.model.AuthContract

class AuthModule: AuthContract {

    val authServiceModel = AuthServiceModel()

    override fun signInWithFirebaseGoogle() {
        signInWithGoogle()
    }

    override fun retrieveCurrentAuthStatus(onComplete: (appAuthStatus: AppAuthStatus) -> Unit) {
        retrieveCurrentUser { user ->
            user?.let {
                authServiceModel.fetchUserStatus(it.email, it.id!!) { authStatus ->
                    onComplete.invoke(authStatus)
                }
            }?: run {
                onComplete.invoke(AppAuthStatus.NONE)
            }
        }
    }

    override fun signOut() {
        signOutApp()
        AppLocalStore.shared.clearValue(key = DataKey.APP_USER_AUTH_STATUS)
    }

    @Composable
    override fun ProvideRegisterUI(showDialog: MutableState<Boolean>, onAuthenticated: () -> Unit,
                                   onCreatedWithoutAuth: () -> Unit, onCancel: () -> Unit) {
        RegisterDialog(showDialog = showDialog, onAuthenticated = onAuthenticated,
            onCreatedWithoutAuth = onCreatedWithoutAuth, onCancel = onCancel)
    }

    @Composable
    override fun ProvideSignUpInviteUI(showDialog: MutableState<Boolean>,
                                       onCancel: () -> Unit) {
        SignUpInviteDialog(showDialog = showDialog, onCancel = onCancel)
    }
}