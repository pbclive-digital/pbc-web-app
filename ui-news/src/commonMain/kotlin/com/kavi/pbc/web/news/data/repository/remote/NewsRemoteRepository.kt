package com.kavi.pbc.web.news.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.news.News
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class NewsRemoteRepository {

    val newsApi = Network.shared.ktorfitClient().createNewsApi()

    suspend fun getActiveNews(): ResultWrapper<BaseResponse<List<News>>> {
        return Network.shared.invokeApiCall { newsApi.getActiveNews() }
    }
}