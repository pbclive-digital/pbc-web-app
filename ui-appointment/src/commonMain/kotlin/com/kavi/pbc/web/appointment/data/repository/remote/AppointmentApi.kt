package com.kavi.pbc.web.appointment.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.appointment.Appointment
import com.kavi.pbc.web.data.appointment.AppointmentRequest
import com.kavi.pbc.web.data.appointment.AppointmentRequestEligibility
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path

interface AppointmentApi {

    @GET("appointment/request/create/eligibility/{userId}")
    suspend fun validateAppointmentReqCreate(@Path("userId") userId: String):
            BaseResponse<AppointmentRequestEligibility>

    @GET("appointment/request/get/user/{userId}")
    suspend fun getAppointmentRequests(@Path("userId") userId: String): BaseResponse<List<AppointmentRequest>>

    @GET("appointment/get/user/{userId}")
    suspend fun getAppointments(@Path("userId") userId: String): BaseResponse<List<Appointment>>

    @POST("appointment/request/create")
    suspend fun createAppointmentRequest(@Body appointmentRequest: AppointmentRequest): BaseResponse<String>

    @PUT("appointment/request/update")
    suspend fun updateAppointmentRequest(@Body appointmentRequest: AppointmentRequest): BaseResponse<AppointmentRequest>

    @DELETE("appointment/request/delete/{appointmentReqId}")
    suspend fun deleteAppointmentRequest(@Path("appointmentReqId") appointmentReqId: String): BaseResponse<String>

    @POST("appointment/create")
    suspend fun createAppointment(@Body appointment: Appointment): BaseResponse<String>

    @PUT("appointment/update")
    suspend fun updateAppointment(@Body appointment: Appointment): BaseResponse<Appointment>

    @DELETE("appointment/delete/{appointmentId}")
    suspend fun deleteAppointment(@Path("appointmentId") appointmentId: String): BaseResponse<String>
}