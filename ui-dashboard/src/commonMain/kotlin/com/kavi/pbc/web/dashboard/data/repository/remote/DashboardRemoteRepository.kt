package com.kavi.pbc.web.dashboard.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.news.News
import com.kavi.pbc.web.data.quote.Quote
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class DashboardRemoteRepository {

    val dashboardApi = Network.shared.ktorfitClient().createDashboardApi()

    suspend fun fetchDashboardEvents(): ResultWrapper<BaseResponse<List<Event>>> {
        return Network.shared.invokeApiCall { dashboardApi.fetchDashboardEvents() }
    }

    suspend fun fetchDashboardDailyQuotes(): ResultWrapper<BaseResponse<List<Quote>>> {
        return Network.shared.invokeApiCall { dashboardApi.fetchDashboardDailyQuotes() }
    }

    suspend fun fetchDashboardNews(): ResultWrapper<BaseResponse<List<News>>> {
        return Network.shared.invokeApiCall { dashboardApi.fetchDashboardNews() }
    }
}