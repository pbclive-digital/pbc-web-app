package com.kavi.pbc.web.datastore

@Suppress("UNCHECKED_CAST")
class AppInMemoryStore {

    companion object {
        val shared = AppInMemoryStore()
    }

    val inMemoryStoreMap = mutableMapOf<String, Any>()

    /**
     * Store any object into `in-memory-store` as key-value pair
     * @param key Object key as a String value
     * @param value Any object to store
     */
    fun storeValue(key: String, value: Any) {
        inMemoryStoreMap[key] = value
    }

    /**
     * Retrieve stored object with given key from `in-memory-store`.
     * [NOTE: Make sure to pass correct generic type, if not this will throw class-casting exception]
     *
     * @param key Object key that need to retrieve value
     * @return Result<T> Result of given generic
     */
    inline fun <reified T>retrieveValue(key: String): Result<T> {
        try {
            val value = inMemoryStoreMap[key] as T
            return if (value is T) {
                Result.success(value)
            } else {
                Result.failure(ClassCastException("Flied"))
            }
        } catch (ex: ClassCastException) {
            println("AppInMemoryStore: Casting: ${ex.printStackTrace()}")
            return Result.failure(ex)
        } catch (ex: NullPointerException) {
            println("AppInMemoryStore: Null-pointer: ${ex.printStackTrace()}")
            return Result.failure(ex)
        }
    }

    fun cleanValue(key: String) {
        inMemoryStoreMap.remove(key)
    }
}