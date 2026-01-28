package com.kavi.pbc.web.network

import com.kavi.pbc.web.network.model.NetConfig
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.network.session.Session
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlin.collections.component1
import kotlin.collections.component2

class Network {

    private var netConfig: NetConfig? = null
    var httpClientInstance: HttpClient = HttpClientFactory.create()

    companion object {
        var shared: Network = Network()
    }

    fun initiate(config: NetConfig) {
        netConfig = config
    }

    fun getBaseUrl(): String {
        return "${netConfig?.scheme}://${netConfig?.domain}"
    }

    @PublishedApi
    internal fun setHeaders(httpRequestBuilder: HttpRequestBuilder) {
        httpRequestBuilder.headers {
            append(HttpHeaders.Accept, "application/json")
            append("X-app-os", "web")
            Session.authToken?.token?.let {
                append(HttpHeaders.Authorization, "Bearer $it")
            }
            Session.user?.let {
                append("X-app-user", it.toString())
            }
        }
        httpRequestBuilder.contentType(ContentType.Application.Json)
    }

    suspend inline fun <reified T> safeRequest(
        block: () -> HttpResponse
    ): ResultWrapper<BaseResponse<T>> {
        return try {
            val response = block()
            when (val status = response.status.value) {
                in 200..299 -> {
                    val body = response.body<BaseResponse<T>>()
                    ResultWrapper.Success(body)
                }
                401 -> {
                    ResultWrapper.UnAuthError(status)
                }
                else -> {
                    val error = runCatching {
                        response.body<Error>()
                    }.getOrNull()

                    ResultWrapper.HttpError(status, error)
                }
            }
        } catch (e: IOException) {
            ResultWrapper.NetworkError
        } catch (e: TimeoutCancellationException) {
            ResultWrapper.NetworkError
        } catch (e: SerializationException) {
            ResultWrapper.HttpError(-1, Error(e.toString()))
        }
    }

    suspend inline fun <reified T> get(
        urlPath: String,
        query: Map<String, Any?> = emptyMap()
    ): ResultWrapper<BaseResponse<T>> = safeRequest<T> {
        httpClientInstance.get("${getBaseUrl()}/$urlPath") {
            query.forEach { (k, v) -> parameter(k, v) }
            setHeaders(this)
        }
    }

    suspend inline fun <reified T, reified E> post(
        urlPath: String,
        body: E?,
        query: Map<String, Any?> = emptyMap()
    ): ResultWrapper<BaseResponse<T>> = safeRequest<T> {
        httpClientInstance.post("${getBaseUrl()}/$urlPath") {
            query.forEach { (k, v) -> parameter(k, v) }
            setHeaders(this)
            setBody(body)
        }
    }

    suspend inline fun <reified T, reified E> put(
        urlPath: String,
        body: E?,
        query: Map<String, Any?> = emptyMap()
    ): ResultWrapper<BaseResponse<T>> = safeRequest<T> {
        httpClientInstance.post("${getBaseUrl()}/$urlPath") {
            query.forEach { (k, v) -> parameter(k, v) }
            setHeaders(this)
            setBody(body)
        }
    }

    suspend inline fun <reified T> delete(
        urlPath: String,
        query: Map<String, Any?> = emptyMap()
    ): ResultWrapper<BaseResponse<T>> = safeRequest<T> {
        httpClientInstance.post("${getBaseUrl()}/$urlPath") {
            query.forEach { (k, v) -> parameter(k, v) }
            setHeaders(this)
        }
    }
}