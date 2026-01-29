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
import io.ktor.http.encodeURLPath

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

    suspend fun getEventRegistration(eventId: String): ResultWrapper<BaseResponse<EventRegistration>> {
        return Network.shared.get<EventRegistration>(urlPath = "event/get/registration/${eventId}")
    }

    suspend fun getEventPotluck(eventId: String): ResultWrapper<BaseResponse<EventPotluck>> {
        return Network.shared.get<EventPotluck>(urlPath = "event/get/potluck/${eventId}")
    }

    suspend fun registerToEvent(eventId: String, eventRegistrationItem: EventRegistrationItem):
            ResultWrapper<BaseResponse<EventRegistration>> {

        val encodedEventId = eventId.encodeURLPath()

        return Network.shared
            .post<EventRegistration, EventRegistrationItem>(
                urlPath = "event/register/${encodedEventId}",
                body = eventRegistrationItem
            )
    }

    suspend fun unregisterFromEvent(eventId: String, userId: String):
            ResultWrapper<BaseResponse<EventRegistration>> {
        val encodeEventId = eventId.encodeURLPath()
        val encodeUserId = userId.encodeURLPath()
        return Network.shared.delete<EventRegistration>(urlPath = "event/unregister/${encodeEventId}/${encodeUserId}")
    }

    suspend fun signUpToPotluck(
        eventId: String,
        potluckItemId: String,
        potluckContributor: EventPotluckContributor
    ): ResultWrapper<BaseResponse<EventPotluck>> {

        val encodedEventId = eventId.encodeURLPath()
        val encodedPotluckItemId = potluckItemId.encodeURLPath()

        return Network.shared
            .post<EventPotluck, EventPotluckContributor>(
                urlPath = "event/potluck/sign-up/${encodedEventId}/${encodedPotluckItemId}",
                body = potluckContributor
            )
    }

    suspend fun signOutFromPotluck(eventId: String, potluckItemId: String, contributorId: String):
            ResultWrapper<BaseResponse<EventPotluck>> {
        val encodeEventId = eventId.encodeURLPath()
        val encodedPotluckItemId = potluckItemId.encodeURLPath()
        val encodedContributorId = contributorId.encodeURLPath()
        return Network.shared.delete<EventPotluck>(
            urlPath = "event/potluck/sign-out/${encodeEventId}/${encodedPotluckItemId}/${encodedContributorId}"
        )
    }

    suspend fun signUpToSelectedSignUpSheet(
        eventId: String,
        sheetId: String,
        contributor: EventSignUpSheetContributor
    ): ResultWrapper<BaseResponse<EventSignUpSheetList>> {

        val encodedEventId = eventId.encodeURLPath()
        val encodedSheetId = sheetId.encodeURLPath()

        return Network.shared
            .post<EventSignUpSheetList, EventSignUpSheetContributor>(
                urlPath = "event/sign-up-sheet/sign-up/${encodedEventId}/${encodedSheetId}",
                body = contributor
            )
    }

    suspend fun signOutFromSelectedSignUpSheet(eventId: String, sheetId: String, contributorId: String):
            ResultWrapper<BaseResponse<EventSignUpSheetList>> {
        val encodeEventId = eventId.encodeURLPath()
        val encodedSheetId = sheetId.encodeURLPath()
        val encodedContributorId = contributorId.encodeURLPath()
        return Network.shared.delete<EventSignUpSheetList>(
            urlPath = "event/sign-up-sheet/sign-out/${encodeEventId}/${encodedSheetId}/${encodedContributorId}"
        )
    }
}