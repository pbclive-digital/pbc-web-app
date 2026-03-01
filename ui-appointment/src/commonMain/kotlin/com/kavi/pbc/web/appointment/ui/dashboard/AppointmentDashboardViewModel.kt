package com.kavi.pbc.web.appointment.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.appointment.data.repository.local.AppointmentLocalRepository
import com.kavi.pbc.web.appointment.data.repository.remote.AppointmentRemoteRepository
import com.kavi.pbc.web.data.appointment.Appointment
import com.kavi.pbc.web.data.appointment.AppointmentRequest
import com.kavi.pbc.web.data.appointment.AppointmentRequestEligibility
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.network.session.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppointmentDashboardViewModel: ViewModel() {

    private val appointmentLocalRepo = AppointmentLocalRepository()
    private val appointmentRemoteRepo = AppointmentRemoteRepository()

    private val _appointmentReqCreateEligibility = MutableStateFlow(AppointmentRequestEligibility())
    val appointmentReqCreateEligibility: StateFlow<AppointmentRequestEligibility> = _appointmentReqCreateEligibility

    private val _appointmentList = MutableStateFlow<List<Appointment>>(mutableListOf())
    val appointmentList: StateFlow<List<Appointment>> = _appointmentList

    private val _appointmentReqList = MutableStateFlow<List<AppointmentRequest>>(mutableListOf())
    val appointmentReqList: StateFlow<List<AppointmentRequest>> = _appointmentReqList

    private val _residentMonkList = MutableStateFlow<List<User>>(mutableListOf())
    val residentMonkList: StateFlow<List<User>> = _residentMonkList

    fun checkAppointmentReqCreateEligibility() {
        Session.user?.id?.let { userId ->
            viewModelScope.launch {
                when(val response = appointmentRemoteRepo.validateAppointmentReqCreate(userId = userId)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                        // Nothing to do
                    }
                    is ResultWrapper.Success -> {
                        response.value.body?.let {
                            _appointmentReqCreateEligibility.value = it
                            fetchResidentMonkList()
                        }
                    }
                }
            }
        }
    }

    fun fetchResidentMonkList() {
        val appConfig = appointmentLocalRepo.retrieveAppConfig()
        appConfig?.let {
            _residentMonkList.value = it.residentMonkList
        }
    }

    fun fetchAppointmentRequests() {
        Session.user?.id?.let { userId ->
            viewModelScope.launch {
                _appointmentReqList.value = emptyList()
                when(val response = appointmentRemoteRepo.getAppointmentRequests(userId = userId)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                        _appointmentReqList.value = emptyList()
                    }
                    is ResultWrapper.Success -> {
                        response.value.body?.let {
                            if (it.isNotEmpty()) {
                                _appointmentReqList.value = it
                            } else {
                                _appointmentReqList.value = emptyList()
                            }
                        }
                    }
                }
            }
        }
    }

    fun fetchAppointments() {
        Session.user?.id?.let { userId ->
            viewModelScope.launch {
                when(val response = appointmentRemoteRepo.getAppointments(userId = userId)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                        _appointmentList.value = emptyList()
                    }
                    is ResultWrapper.Success -> {
                        response.value.body?.let {
                            if (it.isNotEmpty()) {
                                _appointmentList.value = it
                            } else {
                                _appointmentList.value = emptyList()
                            }
                        }
                    }
                }
            }
        }
    }

    fun deleteAppointmentRequest(appointmentReqId: String) {
        viewModelScope.launch {
            when(appointmentRemoteRepo.deleteAppointmentRequest(appointmentReqId)) {
                is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                    // TODO: Notify to UI the failure
                }
                is ResultWrapper.Success -> {
                    fetchAppointmentRequests()
                    checkAppointmentReqCreateEligibility()
                }
            }
        }
    }

    fun deleteAppointment(appointmentId: String) {
        viewModelScope.launch {
            when(appointmentRemoteRepo.deleteAppointment(appointmentId)) {
                is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                    // TODO: Notify to UI the failure
                }
                is ResultWrapper.Success -> {
                    fetchAppointments()
                    checkAppointmentReqCreateEligibility()
                }
            }
        }
    }
}