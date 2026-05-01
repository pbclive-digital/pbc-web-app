package com.kavi.pbc.web.parent.contract.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.parent.contract.CommonContract

interface AuthContract: CommonContract {

    fun signInWithFirebaseGoogle()

    fun retrieveCurrentAuthStatus(onComplete: (appAuthStatus: AppAuthStatus) -> Unit)

    fun signOut()

    fun retrieveUser(onSuccess: (user: User) -> Unit, onFailure: () -> Unit)

    @Composable
    fun ProvideRegisterUI(showDialog: MutableState<Boolean>, onAuthenticated: () -> Unit,
                          onCreatedWithoutAuth: () -> Unit, onCancel: () -> Unit)

    @Composable
    fun ProvideCompleteSignInFlow(showDialog: MutableState<Boolean>, onRegRequired: () -> Unit)

    @Composable
    fun ProvideCompleteSignUpFlow(showDialog: MutableState<Boolean>)

    @Composable
    fun GetUserProfileUI(navController: NavController)
}