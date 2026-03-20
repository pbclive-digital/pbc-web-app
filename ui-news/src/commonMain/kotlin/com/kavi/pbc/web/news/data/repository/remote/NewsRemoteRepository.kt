package com.kavi.pbc.web.news.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.news.News
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

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

    suspend fun uploadNewsImage(newsTitle: String, imageFile: PlatformFile): ResultWrapper<BaseResponse<String>> {
        val byteArray = imageFile.readBytes()

        val multipartBody = MultiPartFormDataContent(
            formData {
                append("newsImage", byteArray, Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "filename=\"$newsTitle\"")
                })
            }
        )

        return Network.shared.invokeApiCall { newsApi.uploadNewsImage(newsTitle, multipartBody) }
    }

    suspend fun createNews(news: News): ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokeApiCall { newsApi.createNews(news) }
    }

    suspend fun updateNews(newsId: String, news: News): ResultWrapper<BaseResponse<News>> {
        return Network.shared.invokeApiCall { newsApi.updateNews(newsId, news) }
    }
}