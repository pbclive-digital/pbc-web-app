package com.kavi.pbc.web.splash.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.config.Config
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class SplashRemoteRepository {
    val splashApi = Network.shared.ktorfitClient().createSplashApi()
    suspend fun fetchConfig(): ResultWrapper<BaseResponse<Config>> {
        return Network.shared.invokeApiCall { splashApi.fetchConfig() }
    }
}