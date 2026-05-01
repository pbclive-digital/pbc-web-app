package com.kavi.pbc.web.local.events.event

import com.kavi.pbc.web.data.auth.AppAuthStatus

sealed class PBCAppEvent {
    data class UserLogin(val authStatus: AppAuthStatus): PBCAppEvent()
}
