package com.kavi.pbc.web.network

import com.kavi.pbc.web.network.model.NetConfig
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.Error
import com.kavi.pbc.web.network.model.AuthException
import com.kavi.pbc.web.network.model.HttpException
import com.kavi.pbc.web.network.model.SystemException
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient

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
        return "${netConfig?.scheme}://${netConfig?.domain}/"
    }

    fun ktorfitClient(): Ktorfit {
        val ktorfit = Ktorfit.Builder()
            .httpClient(httpClientInstance)
            .baseUrl(getBaseUrl())
            .build()

        return ktorfit
    }

    suspend fun <T>invokeApiCall(
        apiCall: suspend () -> BaseResponse<T>
    ): ResultWrapper<BaseResponse<T>> {
        return try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    ResultWrapper.HttpError(throwable.getCode(), throwable.getAppError())
                }
                is AuthException -> {
                    ResultWrapper.HttpError(throwable.getCode(), throwable.getAppError())
                }
                is SystemException -> {
                    ResultWrapper.HttpError(throwable.getCode(), throwable.getAppError())
                }
                else -> {
                    ResultWrapper.HttpError(-1, Error(throwable.toString()))
                }
            }
        }
    }
}