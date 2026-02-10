package com.kavi.pbc.web.splash.data.repository.local

import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.data.config.Config
import com.kavi.pbc.web.datastore.AppLocalStore
import com.kavi.pbc.web.datastore.DataKey

class SplashLocalRepository {

    fun storeAuthStatus(authStatus: AppAuthStatus) {
        AppLocalStore.shared.storeValue(DataKey.APP_USER_AUTH_STATUS, authStatus)
    }

    fun storeAppConfig(config: Config) {
        AppLocalStore.shared.storeValue(DataKey.APP_CONFIG, config)
    }
}