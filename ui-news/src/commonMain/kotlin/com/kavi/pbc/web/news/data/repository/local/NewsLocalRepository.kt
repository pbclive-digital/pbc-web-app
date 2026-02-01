package com.kavi.pbc.web.news.data.repository.local

import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.datastore.AppLocalStore
import com.kavi.pbc.web.datastore.DataKey

class NewsLocalRepository {
    fun getAppAuthStatus(): AppAuthStatus =
        AppLocalStore.shared.retrieveValue<AppAuthStatus>(key = DataKey.APP_USER_AUTH_STATUS)
            ?: run { AppAuthStatus.NONE }
}