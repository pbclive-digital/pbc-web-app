@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.kavi.pbc.web.datastore

@JsName("window")
external val window: Window

external interface LocalStorage {
    fun getItem(key: String): String?
    fun setItem(key: String, value: String)
    fun removeItem(key: String)
}

external interface Window {
    val localStorage: LocalStorage
}

actual object KeyValueStorage {

    private val storage: LocalStorage
        get() = window.localStorage

    actual fun store(key: String, value: String) {
        storage.setItem(key, value)
    }

    actual fun retrieve(key: String): String? {
        return storage.getItem(key)
    }

    actual fun clear(key: String) {
        storage.removeItem(key)
    }
}