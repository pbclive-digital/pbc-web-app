package com.kavi.pbc.web.datastore

import kotlinx.serialization.json.Json

class AppLocalStore {

    companion object {
        val shared = AppLocalStore()
    }

    inline fun <reified T> storeValue(key: String, value: T) {
        val serializeValue = Json.encodeToString(value = value)
        KeyValueStorage.store(key = key, value = serializeValue)
    }

    inline fun <reified T> retrieveValue(key: String): T? {
        val encodedValue = KeyValueStorage.retrieve(key = key)
        encodedValue?.let {
            return Json.decodeFromString<T>(encodedValue)
        }
        return null
    }

    fun clearValue(key: String) {
        KeyValueStorage.clear(key = key)
    }
}