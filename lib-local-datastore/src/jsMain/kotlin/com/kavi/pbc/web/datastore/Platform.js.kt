package com.kavi.pbc.web.datastore

import kotlinx.browser.localStorage

actual object KeyValueStorage {
    actual fun store(key: String, value: String) {
        localStorage.setItem(key, value)
    }

    actual fun retrieve(key: String): String? {
        return localStorage.getItem(key)
    }

    actual fun clear(key: String) {
        localStorage.removeItem(key)
    }
}