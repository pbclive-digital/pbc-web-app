package com.kavi.pbc.web.appointment.data.repository.local

import com.kavi.pbc.web.data.config.Config
import com.kavi.pbc.web.datastore.AppLocalStore
import com.kavi.pbc.web.datastore.DataKey

class AppointmentLocalRepository {

    fun retrieveAppConfig(): Config? {
        return AppLocalStore.shared.retrieveValue<Config>(DataKey.APP_CONFIG)
    }
}