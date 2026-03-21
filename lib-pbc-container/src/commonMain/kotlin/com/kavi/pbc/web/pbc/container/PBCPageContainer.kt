package com.kavi.pbc.web.pbc.container

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.datastore.AppLocalStore
import com.kavi.pbc.web.datastore.DataKey
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import com.kavi.pbc.web.pbc.container.model.ProfileActionConfig
import com.kavi.pbc.web.pbc.container.ui.PageContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PBCPageContainer(
    modifier: Modifier = Modifier,
    user: User? = null,
    content: @Composable () -> Unit
) {
    val showSignUpDialog = remember { mutableStateOf(false) }

    var appAuthStatus by remember {
        mutableStateOf(
            AppLocalStore.shared.retrieveValue<AppAuthStatus>(key = DataKey.APP_USER_AUTH_STATUS)
                ?: run { AppAuthStatus.NONE }
        )
    }

    val profileActionConfig = ProfileActionConfig(
        appAuthStatus = appAuthStatus,
        profileUserImageUrl = user?.profilePicUrl,
        onProfileClick = {
            if (Session.isLogIn()) {
                // Navigate to profile screen
                println("Profile Tap")
            }
        },
        onSignOutClick = {
            // Invoke user authentication
            if (Session.isLogIn()) {
                ContractServiceLocator.locate(AuthContract::class).signOut()
                appAuthStatus = AppAuthStatus.NONE
            }
        },
        onSignUpClick = {
            // Navigate to register screen
            showSignUpDialog.value = true
        },
        onSignInClick = {
            // Invoke sign-in with Firebase-Google
            ContractServiceLocator.locate(AuthContract::class).signInWithFirebaseGoogle()
            ContractServiceLocator.locate(AuthContract::class)
                .retrieveCurrentAuthStatus { authStatus ->
                    appAuthStatus = authStatus
                    if (authStatus == AppAuthStatus.SIGN_UP_REQUIRED) {
                        // Navigate to register screen
                        showSignUpDialog.value = true
                    } else {
                        // Re-login and update auth status
                        AppLocalStore.shared.storeValue(DataKey.APP_USER_AUTH_STATUS, authStatus)
                    }
                }
        }
    )

    if (showSignUpDialog.value) {
        ContractServiceLocator.locate(AuthContract::class).ProvideRegisterUI(
            showDialog = showSignUpDialog,
            onAuthenticated = {
                appAuthStatus = AppAuthStatus.SIGN_IN
                // Re-login and update auth status
                AppLocalStore.shared.storeValue(DataKey.APP_USER_AUTH_STATUS, appAuthStatus)
                showSignUpDialog.value = false
            },
            onCreatedWithoutAuth = {
                appAuthStatus = AppAuthStatus.FAILED
                // Re-login and update auth status
                AppLocalStore.shared.storeValue(DataKey.APP_USER_AUTH_STATUS, appAuthStatus)
                showSignUpDialog.value = false
            },
            onCancel = {
                showSignUpDialog.value = false
            }
        )
    }

    PageContainer(modifier = modifier, profileActionConfig = profileActionConfig, content = content)
}