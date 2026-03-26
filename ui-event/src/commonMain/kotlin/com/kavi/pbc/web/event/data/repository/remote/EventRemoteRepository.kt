package com.kavi.pbc.web.event.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.event.potluck.EventPotluck
import com.kavi.pbc.web.data.event.potluck.EventPotluckContributor
import com.kavi.pbc.web.data.event.register.EventRegistration
import com.kavi.pbc.web.data.event.register.EventRegistrationItem
import com.kavi.pbc.web.data.event.signup.EventSignUpSheetContributor
import com.kavi.pbc.web.data.event.signup.EventSignUpSheetList
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class EventRemoteRepository {

    val eventApi = Network.shared.ktorfitClient().createEventApi()

    suspend fun getDraftEvents(): ResultWrapper<BaseResponse<List<Event>>> {
        return Network.shared.invokeApiCall { eventApi.getDraftEvents() }
    }
    suspend fun getUpcomingEvents(): ResultWrapper<BaseResponse<List<Event>>> {
        return Network.shared.invokeApiCall { eventApi.getUpcomingEvents() }
    }

    suspend fun getPastEvents(): ResultWrapper<BaseResponse<List<Event>>> {
        return Network.shared.invokeApiCall { eventApi.getPastEvents() }
    }

    suspend fun getPastEventsWithLimit(limit: Int): ResultWrapper<BaseResponse<List<Event>>> {
        return Network.shared.invokeApiCall { eventApi.getPastEventsWithLimit(limit = limit) }
    }

    suspend fun getEventDetails(eventId: String): ResultWrapper<BaseResponse<Event>> {
        return Network.shared.invokeApiCall { eventApi.getEventDetails(eventId = eventId) }
    }

    suspend fun getSignUpSheetList(eventId: String): ResultWrapper<BaseResponse<EventSignUpSheetList>> {
        return Network.shared.invokeApiCall { eventApi.getSignUpSheetList(eventId = eventId) }
    }

    suspend fun getEventRegistration(eventId: String): ResultWrapper<BaseResponse<EventRegistration>> {
        return Network.shared.invokeApiCall { eventApi.getEventRegistration(eventId = eventId) }
    }

    suspend fun getEventPotluck(eventId: String): ResultWrapper<BaseResponse<EventPotluck>> {
        return Network.shared.invokeApiCall { eventApi.getEventPotluck(eventId = eventId) }
    }

    suspend fun registerToEvent(eventId: String, eventRegistrationItem: EventRegistrationItem):
            ResultWrapper<BaseResponse<EventRegistration>> {
        return Network.shared.invokeApiCall {
            eventApi.registerToEvent(eventId = eventId, eventRegistrationItem = eventRegistrationItem)
        }
    }

    suspend fun unregisterFromEvent(eventId: String, userId: String):
            ResultWrapper<BaseResponse<EventRegistration>> {
        return Network.shared.invokeApiCall {
            eventApi.unregisterFromEvent(eventId = eventId, userId = userId)
        }
    }

    suspend fun signUpToPotluck(
        eventId: String,
        potluckItemId: String,
        potluckContributor: EventPotluckContributor
    ): ResultWrapper<BaseResponse<EventPotluck>> {

        return Network.shared.invokeApiCall {
            eventApi.signUpToPotluck(eventId = eventId, potluckItemId = potluckItemId, potluckContributor = potluckContributor)
        }
    }

    suspend fun signOutFromPotluck(eventId: String, potluckItemId: String, contributorId: String):
            ResultWrapper<BaseResponse<EventPotluck>> {
        return Network.shared.invokeApiCall {
            eventApi.signOutFromPotluck(
                eventId = eventId, potluckItemId = potluckItemId,
                contributorId = contributorId
            )
        }
    }

    suspend fun signUpToSelectedSignUpSheet(
        eventId: String,
        sheetId: String,
        contributor: EventSignUpSheetContributor
    ): ResultWrapper<BaseResponse<EventSignUpSheetList>> {
        return Network.shared.invokeApiCall {
            eventApi.signUpToSelectedSignUpSheet(
                eventId = eventId, sheetId = sheetId,
                contributor = contributor
            )
        }
    }

    suspend fun signOutFromSelectedSignUpSheet(eventId: String, sheetId: String, contributorId: String):
            ResultWrapper<BaseResponse<EventSignUpSheetList>> {
        return Network.shared.invokeApiCall {
            eventApi.signOutFromSelectedSignUpSheet(
                eventId = eventId, sheetId = sheetId, contributorId = contributorId
            )
        }
    }
}