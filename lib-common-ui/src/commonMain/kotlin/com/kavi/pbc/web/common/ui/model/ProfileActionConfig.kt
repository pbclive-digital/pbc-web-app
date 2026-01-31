package com.kavi.pbc.web.common.ui.model

import com.kavi.pbc.web.data.auth.AppAuthStatus

data class ProfileActionConfig(
    val appAuthStatus: AppAuthStatus? = AppAuthStatus.NONE,
    val profileUserImageUrl: String? = null,
    val onProfileClick: () -> Unit,
    val onSignOutClick: () -> Unit,
    val onSignUpClick: () -> Unit,
    val onSignInClick: () -> Unit,
)
