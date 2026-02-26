package com.kavi.pbc.web.appointment.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.appointment.Appointment
import com.kavi.pbc.web.data.appointment.AppointmentRequest
import com.kavi.pbc.web.data.appointment.AppointmentRequestEligibility
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class AppointmentRemoteRepository {


    suspend fun validateAppointmentReqCreate(userId: String): ResultWrapper<BaseResponse<AppointmentRequestEligibility>> {
        return Network.shared.get<AppointmentRequestEligibility>(urlPath = "appointment/request/create/eligibility/$userId")
    }

    suspend fun getAppointmentRequests(userId: String): ResultWrapper<BaseResponse<List<AppointmentRequest>>> {
        return Network.shared.get<List<AppointmentRequest>>(urlPath = "appointment/request/get/user/$userId")
    }

    suspend fun getAppointments(userId: String): ResultWrapper<BaseResponse<List<Appointment>>> {
        return Network.shared.get<List<Appointment>>(urlPath = "appointment/get/user/$userId")
    }

    suspend fun createAppointmentRequest(appointmentRequest: AppointmentRequest):
            ResultWrapper<BaseResponse<String>> {
        return Network.shared.post<String, AppointmentRequest>(
            urlPath = "appointment/request/create",
            body = appointmentRequest
        )
    }

    suspend fun updateAppointmentRequest(appointmentRequest: AppointmentRequest):
            ResultWrapper<BaseResponse<AppointmentRequest>> {
        return Network.shared.put<AppointmentRequest, AppointmentRequest>(
            urlPath = "appointment/request/update",
            body = appointmentRequest
        )
    }

    suspend fun deleteAppointmentRequest(appointmentReqId: String): ResultWrapper<BaseResponse<String>> {
        return Network.shared.delete<String>(urlPath = "appointment/request/delete/$appointmentReqId")
    }
}