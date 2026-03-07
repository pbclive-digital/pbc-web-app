package com.kavi.pbc.web.appointment.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.appointment.Appointment
import com.kavi.pbc.web.data.appointment.AppointmentRequest
import com.kavi.pbc.web.data.appointment.AppointmentRequestEligibility
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class AppointmentRemoteRepository {

    val appointmentApi: AppointmentApi = Network.shared.ktorfitClient().createAppointmentApi()

    suspend fun validateAppointmentReqCreate(userId: String): ResultWrapper<BaseResponse<AppointmentRequestEligibility>> {
        return Network.shared.invokeApiCall {
            appointmentApi.validateAppointmentReqCreate(userId = userId)
        }
    }

    suspend fun getAppointmentRequests(userId: String): ResultWrapper<BaseResponse<List<AppointmentRequest>>> {
        return Network.shared.invokeApiCall {
            appointmentApi.getAppointmentRequests(userId = userId)
        }
    }

    suspend fun getAppointments(userId: String): ResultWrapper<BaseResponse<List<Appointment>>> {
        return Network.shared.invokeApiCall {
            appointmentApi.getAppointments(userId = userId)
        }
    }

    suspend fun createAppointmentRequest(appointmentRequest: AppointmentRequest):
            ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokeApiCall {
            appointmentApi.createAppointmentRequest(appointmentRequest = appointmentRequest)
        }
    }

    suspend fun updateAppointmentRequest(appointmentRequest: AppointmentRequest):
            ResultWrapper<BaseResponse<AppointmentRequest>> {
        return Network.shared.invokeApiCall {
            appointmentApi.updateAppointmentRequest(appointmentRequest = appointmentRequest)
        }
    }

    suspend fun deleteAppointmentRequest(appointmentReqId: String): ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokeApiCall {
            appointmentApi.deleteAppointmentRequest(appointmentReqId = appointmentReqId)
        }
    }

    suspend fun createAppointment(appointment: Appointment):
            ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokeApiCall {
            appointmentApi.createAppointment(appointment = appointment)
        }
    }

    suspend fun updateAppointment(appointment: Appointment):
            ResultWrapper<BaseResponse<Appointment>> {
        return Network.shared.invokeApiCall {
            appointmentApi.updateAppointment(appointment = appointment)
        }
    }

    suspend fun deleteAppointment(appointmentId: String): ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokeApiCall {
            appointmentApi.deleteAppointment(appointmentId = appointmentId)
        }
    }
}