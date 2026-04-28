package com.kavi.pbc.web.event.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.email.EmailGroupHeading
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.event.PublishEventRequest
import com.kavi.pbc.web.data.event.potluck.EventPotluck
import com.kavi.pbc.web.data.event.potluck.EventPotluckContributor
import com.kavi.pbc.web.data.event.potluck.PotluckDownloadLink
import com.kavi.pbc.web.data.event.register.EventRegistration
import com.kavi.pbc.web.data.event.register.EventRegistrationItem
import com.kavi.pbc.web.data.event.register.RegistrationDownloadLink
import com.kavi.pbc.web.data.event.signup.EventSignUpSheetContributor
import com.kavi.pbc.web.data.event.signup.EventSignUpSheetList
import com.kavi.pbc.web.data.event.signup.SignUpSheetDownloadLink
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.request.forms.MultiPartFormDataContent

interface EventApi {

    @GET("event/get/draft")
    suspend fun getDraftEvents(): BaseResponse<List<Event>>

    @GET("event/get/upcoming")
    suspend fun getUpcomingEvents(): BaseResponse<List<Event>>

    @GET("event/get/recurring")
    suspend fun getRecurringEvents(): BaseResponse<List<Event>>

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

    @GET("email-group/get/all")
    suspend fun getEmailGroupHeadings(): BaseResponse<List<EmailGroupHeading>>

    @PUT("event/put/publish/{eventId}")
    suspend fun publishDraftEvent(@Path("eventId") eventId: String, @Body event: Event): BaseResponse<Event>

    @PUT("event/v2/put/publish/{eventId}")
    suspend fun publishDraftEvent(@Path("eventId") eventId: String, @Body publishEventReq: PublishEventRequest): BaseResponse<Event>

    @DELETE("event/delete/{eventId}")
    suspend fun deleteEvent(@Path("eventId") eventId: String): BaseResponse<String>

    @Multipart
    @POST("event/add/image/{eventName}")
    suspend fun uploadEventImage(@Path("eventName") eventName: String, @Body file: MultiPartFormDataContent): BaseResponse<String>

    @POST("event/create")
    suspend fun createNewEvent(@Body event: Event): BaseResponse<String>

    @PUT("event/update/{eventId}")
    suspend fun updateEvent(@Path("eventId") eventId: String, @Body event: Event): BaseResponse<Event>

    @GET("event/get/registration/download-link/{eventId}")
    suspend fun getEventRegistrationDownloadLink(@Path("eventId") eventId: String): BaseResponse<RegistrationDownloadLink>

    @GET("event/get/potluck/download-link/{eventId}")
    suspend fun getEventPotluckDownloadLink(@Path("eventId") eventId: String): BaseResponse<PotluckDownloadLink>

    @GET("event/get/sign-up-sheet/download-link/{eventId}/{sheetId}")
    suspend fun getEventSignUpSheetDownloadLink(@Path("eventId") eventId: String, @Path("sheetId") sheetId: String): BaseResponse<SignUpSheetDownloadLink>
}