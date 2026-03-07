package com.kavi.pbc.web.news.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.news.News
import de.jensklingenberg.ktorfit.http.GET

interface NewsApi {

    @GET("news/get/active")
    suspend fun getActiveNews(): BaseResponse<List<News>>
}