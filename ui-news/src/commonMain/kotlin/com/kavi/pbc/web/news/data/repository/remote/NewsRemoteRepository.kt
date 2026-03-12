package com.kavi.pbc.web.news.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.news.News
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class NewsRemoteRepository {

    val newsApi = Network.shared.ktorfitClient().createNewsApi()

    suspend fun getActiveNews(): ResultWrapper<BaseResponse<MutableList<News>>> {
        return Network.shared.invokeApiCall { newsApi.getActiveNews() }
    }

    suspend fun getDraftNews(): ResultWrapper<BaseResponse<MutableList<News>>> {
        return Network.shared.invokeApiCall { newsApi.fetchDraftNewsList() }
    }

    suspend fun publishDraftNews(newsId: String, news: News): ResultWrapper<BaseResponse<News>> {
        return Network.shared.invokeApiCall { newsApi.publishDraftNews(newsId, news) }
    }

    suspend fun deleteNews(newsId: String): ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokeApiCall { newsApi.deleteNews(newsId) }
    }
}