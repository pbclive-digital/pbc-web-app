package com.kavi.pbc.web.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.kavi.pbc.web.auth.service.auth.AuthServiceModel
import com.kavi.pbc.web.auth.ui.invite.SignUpInviteDialog
import com.kavi.pbc.web.auth.ui.profile.UserProfileUI
import com.kavi.pbc.web.auth.ui.register.RegisterDialog
import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.datastore.AppLocalStore
import com.kavi.pbc.web.datastore.DataKey
import com.kavi.pbc.web.local.events.PBCEventBus
import com.kavi.pbc.web.local.events.event.PBCAppEvent
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.parent.contract.model.AuthContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    override fun retrieveUser(
        onSuccess: (user: User) -> Unit,
        onFailure: () -> Unit
    ) {
        retrieveCurrentUser { user ->
            user?.let {
                authServiceModel.fetchUser(it.id!!, onComplete = { authStatus ->
                    when(authStatus) {
                        AppAuthStatus.SIGN_IN -> {
                            onSuccess.invoke(Session.user!!)
                        }
                        else -> {
                            onFailure.invoke()
                        }
                    }
                })
            }?: run {
                onFailure.invoke()
            }
        }
    }

    @Composable
    override fun ProvideRegisterUI(showDialog: MutableState<Boolean>, onAuthenticated: () -> Unit,
                                   onCreatedWithoutAuth: () -> Unit, onCancel: () -> Unit) {
        RegisterDialog(showDialog = showDialog, onAuthenticated = onAuthenticated,
            onCreatedWithoutAuth = onCreatedWithoutAuth, onCancel = onCancel)
    }

    @Composable
    override fun ProvideCompleteSignInFlow(showDialog: MutableState<Boolean>, onRegRequired: () -> Unit) {

        val coroutineScope = CoroutineScope(Dispatchers.Default)

        SignUpInviteDialog(showDialog = showDialog, {
            signInWithGoogle()
            retrieveCurrentUser { user ->
                user?.let {
                    authServiceModel.fetchUserStatus(it.email, it.id!!) { authStatus ->
                        if (authStatus == AppAuthStatus.SIGN_UP_REQUIRED) {
                            // Navigate to register screen
                            onRegRequired.invoke()
                        } else {
                            // Re-login and update auth status
                            AppLocalStore.shared.storeValue(DataKey.APP_USER_AUTH_STATUS, authStatus)
                            // Emit login event
                            coroutineScope.launch {
                                PBCEventBus.publish(PBCAppEvent.UserLogin(authStatus = authStatus))
                            }
                        }
                    }
                }?: run {
                    // Set to default
                    AppLocalStore.shared.storeValue(DataKey.APP_USER_AUTH_STATUS, AppAuthStatus.NONE)
                }
            }
            showDialog.value = false
        })
    }

    @Composable
    override fun ProvideCompleteSignUpFlow(showDialog: MutableState<Boolean>) {
        val coroutineScope = CoroutineScope(Dispatchers.Default)

        RegisterDialog(
            showDialog = showDialog,
            onAuthenticated = {
                // Re-login and update auth status
                AppLocalStore.shared.storeValue(DataKey.APP_USER_AUTH_STATUS, AppAuthStatus.SIGN_IN)
                // Emit login event
                coroutineScope.launch {
                    PBCEventBus.publish(PBCAppEvent.UserLogin(authStatus = AppAuthStatus.SIGN_IN))
                }
                showDialog.value = false
            },
            onCreatedWithoutAuth = {
                // Re-login and update auth status
                AppLocalStore.shared.storeValue(DataKey.APP_USER_AUTH_STATUS, AppAuthStatus.FAILED)
                showDialog.value = false
            },
            onCancel = {
                showDialog.value = false
            }
        )
    }

    @Composable
    override fun GetUserProfileUI(navController: NavController) {
        UserProfileUI(navController = navController)
    }
}