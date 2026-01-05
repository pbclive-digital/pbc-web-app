package com.kavi.pbc.web.network

import com.kavi.pbc.web.network.model.NetConfig
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.network.session.Session
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class Network {

    private var netConfig: NetConfig? = null
    private var httpClientInstance: HttpClient? = null

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
    internal fun getHttpClientInstance(): HttpClient {
        httpClientInstance?.let {
            return it
        }?: run {
            httpClientInstance =  HttpClient() {
                install(Logging) {
                    level = LogLevel.ALL
                }
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                    })
                }
            }
            return httpClientInstance!!
        }
    }

    @PublishedApi
    internal fun setHeaders(httpRequestBuilder: HttpRequestBuilder) {
        httpRequestBuilder.headers {
            append(HttpHeaders.Accept, "application/json")
            append("X-app-os", "web")
            Session.authToken?.token?.let {
                append(HttpHeaders.Authorization, it)
            }
            Session.user?.let {
                append("X-app-user", it.toString())
            }
        }
        httpRequestBuilder.contentType(ContentType.Application.Json)
    }

    suspend inline fun <reified T>invokeGET(urlPath: String): ResultWrapper<BaseResponse<T>> {
        return try {
            val response = getHttpClientInstance().get {
                url("${getBaseUrl()}/$urlPath")
                setHeaders(this)
            }
            val configRes: BaseResponse<T> = response.body<BaseResponse<T>>()
            ResultWrapper.Success(configRes)
        } catch (throwable: Exception) {
            ResultWrapper.HttpError(-1, Error(throwable.toString()))
        }
    }

    suspend inline fun <reified T, reified E>invokePOST(urlPath: String, params: E): ResultWrapper<BaseResponse<T>> {
        return try {
            val response = getHttpClientInstance().post {
                url("${getBaseUrl()}/$urlPath")
                setHeaders(this)
                setBody(params)
            }
            val configRes: BaseResponse<T> = response.body<BaseResponse<T>>()
            ResultWrapper.Success(configRes)
        } catch (throwable: Exception) {
            ResultWrapper.HttpError(-1, Error(throwable.toString()))
        }
    }
}