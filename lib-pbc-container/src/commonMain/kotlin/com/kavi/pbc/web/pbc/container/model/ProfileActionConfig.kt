package com.kavi.pbc.web.pbc.container.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.kavi.pbc.web.data.auth.AppAuthStatus

data class ProfileActionConfig(
    val appAuthStatus: AppAuthStatus? = AppAuthStatus.NONE,
    val profileUserImageUrl: MutableState<String?> = mutableStateOf(null),
    val onProfileClick: () -> Unit,
    val onSignOutClick: () -> Unit,
    val onSignUpClick: () -> Unit,
    val onSignInClick: () -> Unit,
)
