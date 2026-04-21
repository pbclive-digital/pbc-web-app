package com.kavi.pbc.web.pbc.container.model

import com.kavi.pbc.web.data.auth.AppAuthStatus

data class ProfileActionConfig(
    val appAuthStatus: AppAuthStatus? = AppAuthStatus.NONE,
    val profileUserImageUrl: String?,
    val onSignOutClick: () -> Unit,
    val onSignUpClick: () -> Unit,
    val onSignInClick: () -> Unit,
)
