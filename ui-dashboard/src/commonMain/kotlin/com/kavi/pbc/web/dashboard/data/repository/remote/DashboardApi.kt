package com.kavi.pbc.web.dashboard.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.news.News
import com.kavi.pbc.web.data.quote.Quote
import de.jensklingenberg.ktorfit.http.GET

interface DashboardApi {

    @GET("dashboard/get/events")
    suspend fun fetchDashboardEvents(): BaseResponse<List<Event>>

    @GET("dashboard/get/daily/quotes")
    suspend fun fetchDashboardDailyQuotes(): BaseResponse<List<Quote>>

    @GET("dashboard/get/news")
    suspend fun fetchDashboardNews(): BaseResponse<List<News>>
}