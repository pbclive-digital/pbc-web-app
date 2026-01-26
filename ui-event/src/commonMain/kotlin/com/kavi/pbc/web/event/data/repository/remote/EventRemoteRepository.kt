package com.kavi.pbc.web.event.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.event.signup.EventSignUpSheetList
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class EventRemoteRepository {

    suspend fun getUpcomingEvents(): ResultWrapper<BaseResponse<List<Event>>> {
        return Network.shared.get<List<Event>>(urlPath = "event/get/upcoming")
    }

    suspend fun getPastEvents(): ResultWrapper<BaseResponse<List<Event>>> {
        return Network.shared.get<List<Event>>(urlPath = "event/get/past")
    }

    suspend fun getPastEventsWithLimit(limit: Int): ResultWrapper<BaseResponse<List<Event>>> {
        return Network.shared.get<List<Event>>(urlPath = "event/get/past/${limit}")
    }

    suspend fun getEventDetails(eventId: String): ResultWrapper<BaseResponse<Event>> {
        return Network.shared.get<Event>(urlPath = "event/get/${eventId}")
    }

    suspend fun getSignUpSheetList(eventId: String): ResultWrapper<BaseResponse<EventSignUpSheetList>> {
        return Network.shared.get<EventSignUpSheetList>(urlPath = "event/get/sign-up-sheet/${eventId}")
    }
}