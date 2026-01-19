package com.kavi.pbc.web.datastore

expect object KeyValueStorage {
    fun store(key: String, value: String)
    fun retrieve(key: String): String?
    fun clear(key: String)
}