package com.kavi.pbc.web.splash.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.config.Config
import de.jensklingenberg.ktorfit.http.GET

interface SplashApi {

    @GET("config/get/v1")
    suspend fun fetchConfig(): BaseResponse<Config>
}