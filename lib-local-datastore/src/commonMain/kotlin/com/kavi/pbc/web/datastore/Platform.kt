@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.kavi.pbc.web.datastore

expect object KeyValueStorage {
    fun store(key: String, value: String)
    fun retrieve(key: String): String?
    fun clear(key: String)
}