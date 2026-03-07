package com.kavi.pbc.web.network

import com.kavi.pbc.web.network.model.AuthException
import com.kavi.pbc.web.network.model.HttpException
import com.kavi.pbc.web.network.model.NetworkException
import com.kavi.pbc.web.network.model.SystemException
import com.kavi.pbc.web.network.session.Session
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal object HttpClientFactory {

    fun create(): HttpClient = HttpClient{
        expectSuccess = false

        install(Logging) {
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = true
                explicitNulls = false
            })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 60_000
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            header("X-app-os", "web")
            Session.authToken?.token?.let {
                header(HttpHeaders.Authorization, "Bearer $it")
            }
            Session.user?.let {
                header("X-app-user", Json.encodeToString(it))
            }
        }

        HttpResponseValidator {
            validateResponse { response ->
                // Logic check: (statusCode <= 402 && statusCode <= 499) might be a typo.
                // Use: statusCode in 402..499
                when (val statusCode = response.status.value) {
                    401 -> throw AuthException()
                    in 400..499 -> throw HttpException(statusCode, response.status.description)
                    in 500..599 -> throw SystemException(statusCode, response.status.description)
                }
            }
            handleResponseExceptionWithRequest { cause, _ ->
                // If it's already one of our custom exceptions, just re-throw it!
                if (cause is AuthException || cause is HttpException || cause is SystemException) {
                    throw cause
                }
                // Otherwise, it's a real network failure (e.g., Timeout, DNS)
                throw NetworkException(cause.message ?: "Unknown Network Error")
            }
        }
    }
}