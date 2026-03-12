package com.kavi.pbc.web.news.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.news.News
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path

interface NewsApi {

    @GET("news/get/active")
    suspend fun getActiveNews(): BaseResponse<MutableList<News>>

    @GET("news/get/draft")
    suspend fun fetchDraftNewsList(): BaseResponse<MutableList<News>>

    @PUT("news/update/publish/{newsId}")
    suspend fun publishDraftNews(@Path("newsId") newsId: String, @Body news: News): BaseResponse<News>

    @DELETE("news/delete/{newsId}")
    suspend fun deleteNews(@Path("newsId") newsId: String): BaseResponse<String>
}