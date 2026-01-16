package com.kavi.pbc.web.event.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class EventRemoteRepository {

    suspend fun getUpcomingEvents(): ResultWrapper<BaseResponse<List<Event>>> {
        return Network.shared.invokeGET<List<Event>>(urlPath = "event/get/upcoming")
    }

    suspend fun getPastEvents(): ResultWrapper<BaseResponse<List<Event>>> {
        return Network.shared.invokeGET<List<Event>>(urlPath = "event/get/past")
    }

    suspend fun getPastEventsWithLimit(limit: Int): ResultWrapper<BaseResponse<List<Event>>> {
        return Network.shared.invokeGET<List<Event>>(urlPath = "event/get/past/${limit}")
    }
}