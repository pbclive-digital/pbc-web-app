package com.kavi.pbc.web.event.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.event.potluck.EventPotluck
import com.kavi.pbc.web.data.event.potluck.EventPotluckContributor
import com.kavi.pbc.web.data.event.register.EventRegistration
import com.kavi.pbc.web.data.event.register.EventRegistrationItem
import com.kavi.pbc.web.data.event.signup.EventSignUpSheetContributor
import com.kavi.pbc.web.data.event.signup.EventSignUpSheetList
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface EventApi {

    @GET("event/get/upcoming")
    suspend fun getUpcomingEvents(): BaseResponse<List<Event>>

    @GET("event/get/past")
    suspend fun getPastEvents(): BaseResponse<List<Event>>

    @GET("event/get/past/{limit}")
    suspend fun getPastEventsWithLimit(@Path("limit") limit: Int): BaseResponse<List<Event>>

    @GET("event/get/{eventId}")
    suspend fun getEventDetails(@Path("eventId") eventId: String): BaseResponse<Event>

    @GET("event/get/sign-up-sheet/{eventId}")
    suspend fun getSignUpSheetList(@Path("eventId") eventId: String): BaseResponse<EventSignUpSheetList>

    @GET("event/get/registration/{eventId}")
    suspend fun getEventRegistration(@Path("eventId") eventId: String): BaseResponse<EventRegistration>

    @GET("event/get/potluck/{eventId}")
    suspend fun getEventPotluck(@Path("eventId") eventId: String): BaseResponse<EventPotluck>

    @POST("event/register/{eventId}")
    suspend fun registerToEvent(@Path("eventId") eventId: String,
                                @Body eventRegistrationItem: EventRegistrationItem): BaseResponse<EventRegistration>

    @DELETE("event/unregister/{eventId}/{userId}")
    suspend fun unregisterFromEvent(@Path("eventId") eventId: String,
                                    @Path("userId") userId: String): BaseResponse<EventRegistration>

    @POST("event/potluck/sign-up/{eventId}/{potluckItemId}")
    suspend fun signUpToPotluck(@Path("eventId") eventId: String,
                                @Path("potluckItemId") potluckItemId: String,
                                @Body potluckContributor: EventPotluckContributor): BaseResponse<EventPotluck>

    @DELETE("event/potluck/sign-out/{eventId}/{potluckItemId}/{contributorId}")
    suspend fun signOutFromPotluck(@Path("eventId") eventId: String,
                                   @Path("potluckItemId") potluckItemId: String,
                                   @Path("contributorId") contributorId: String): BaseResponse<EventPotluck>

    @POST("event/sign-up-sheet/sign-up/{eventId}/{sheetId}")
    suspend fun signUpToSelectedSignUpSheet(@Path("eventId") eventId: String,
                                @Path("sheetId") sheetId: String,
                                @Body contributor: EventSignUpSheetContributor): BaseResponse<EventSignUpSheetList>

    @DELETE("event/sign-up-sheet/sign-out/{eventId}/{sheetId}/{contributorId}")
    suspend fun signOutFromSelectedSignUpSheet(@Path("eventId") eventId: String,
                                   @Path("sheetId") sheetId: String,
                                   @Path("contributorId") contributorId: String): BaseResponse<EventSignUpSheetList>
}