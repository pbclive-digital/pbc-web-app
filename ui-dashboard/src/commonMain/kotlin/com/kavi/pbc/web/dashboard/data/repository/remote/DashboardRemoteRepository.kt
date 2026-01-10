package com.kavi.pbc.web.dashboard.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.quote.Quote
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class DashboardRemoteRepository {

    suspend fun fetchDashboardEvents(): ResultWrapper<BaseResponse<List<Event>>> {
        return Network.shared.invokeGET<List<Event>>(urlPath = "dashboard/get/events")
    }

    suspend fun fetchDashboardDailyQuotes(): ResultWrapper<BaseResponse<List<Quote>>> {
        return Network.shared.invokeGET<List<Quote>>(urlPath = "dashboard/get/daily/quotes")
    }
}