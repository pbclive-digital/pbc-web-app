package com.kavi.pbc.web.auth

import com.kavi.pbc.web.data.user.User

expect fun signInWithGoogle()

expect fun retrieveCurrentUser(onUserAvailable: (user: User?) -> Unit)

expect fun signOutApp()