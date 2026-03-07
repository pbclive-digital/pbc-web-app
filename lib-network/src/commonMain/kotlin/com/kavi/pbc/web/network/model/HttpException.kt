package com.kavi.pbc.web.network.model

import com.kavi.pbc.web.data.Error

abstract class AppException(): Exception() {
    abstract fun getCode(): Int
    abstract fun getAppError(): Error
}

class NetworkException(private val errorMessage: String?): AppException() {
    override fun getCode(): Int = -1
    override fun getAppError(): Error = Error(message = "Network Failure: $errorMessage")
}

class AuthException(): AppException() {
    override fun getCode(): Int = 401
    override fun getAppError(): Error = Error(message = "Authentication Failure")
}

class HttpException(private val statusCode: Int, private val errorMessage: String): AppException() {
    override fun getCode(): Int = statusCode
    override fun getAppError(): Error = Error(message = errorMessage)
}

class SystemException(private val statusCode: Int, private val errorMessage: String): AppException() {
    override fun getCode(): Int = statusCode
    override fun getAppError(): Error = Error(message = errorMessage)
}