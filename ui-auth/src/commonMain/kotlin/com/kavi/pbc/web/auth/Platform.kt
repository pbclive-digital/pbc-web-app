package com.kavi.pbc.web.auth

import androidx.compose.runtime.MutableState
import com.kavi.pbc.web.data.user.User

expect fun platform(): String

expect fun signInWithGoogle()

expect fun retrieveCurrentUser(userState: MutableState<User?>): (() -> Unit)?

expect fun retrieveCurrentUser(onUserAvailable: (user: User?) -> Unit)

expect fun signOutApp()