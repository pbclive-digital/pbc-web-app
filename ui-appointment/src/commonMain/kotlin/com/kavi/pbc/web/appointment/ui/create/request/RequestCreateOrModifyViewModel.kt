package com.kavi.pbc.web.appointment.ui.create.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.appointment.data.model.AppointmentCreateOrModifyUiStatus
import com.kavi.pbc.web.appointment.data.repository.local.AppointmentLocalRepository
import com.kavi.pbc.web.appointment.data.repository.remote.AppointmentRemoteRepository
import com.kavi.pbc.web.data.appointment.AppointmentRequest
import com.kavi.pbc.web.data.appointment.AppointmentRequestType
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.network.session.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.collections.set

class RequestCreateOrModifyViewModel: ViewModel() {

    private val appointmentRemoteRepo = AppointmentRemoteRepository()
    private val appointmentLocalRepo = AppointmentLocalRepository()

    // Make this question is nullable, because need to clear the appointment request object when creation or modify complete
    private val _createOrModifyAppointmentReq: MutableStateFlow<AppointmentRequest?> = MutableStateFlow(AppointmentRequest(
        user = User(email = "")
    ))
    val createOrModifyAppointmentReq: StateFlow<AppointmentRequest?> = _createOrModifyAppointmentReq

    private val _appointmentReqUiStatus = MutableStateFlow(AppointmentCreateOrModifyUiStatus.NONE)
    val appointmentReqUiStatus: StateFlow<AppointmentCreateOrModifyUiStatus> = _appointmentReqUiStatus

    private val _residenceMonkList = MutableStateFlow<List<String>>(mutableListOf())
    val residenceMonkList: StateFlow<List<String>> = _residenceMonkList

    val monkMapping = mutableMapOf<String, User>()

    fun setModifyAppointmentReq(appointmentReq: AppointmentRequest) {
        _createOrModifyAppointmentReq.value = appointmentReq
    }

    fun initiateNewAppointmentReq(selectedMonk: User? = null) {
        Session.user?.let {
            _createOrModifyAppointmentReq.value = AppointmentRequest(
                userId = it.id!!,
                user = it,
                selectedMonk = selectedMonk,
                selectedMonkId = selectedMonk?.id?: "any"
            )
        }
    }

    fun getResidentMonkList() {
        val monkList = mutableListOf<String>()
        monkList.add("Any Bhanthe")
        val appConfig = appointmentLocalRepo.retrieveAppConfig()
        appConfig?.let {
            it.residentMonkList.forEach { monk ->
                val monkDisplayName = "Bhanthe ${monk.lastName}"
                monkMapping[monkDisplayName] = monk
                monkList.add(monkDisplayName)
            }
        }

        _residenceMonkList.value = monkList
    }

    fun clearAppointmentRequest() {
        _createOrModifyAppointmentReq.value = null
    }

    fun revokeAppointmentReqUiStatus() {
        _appointmentReqUiStatus.value = AppointmentCreateOrModifyUiStatus.NONE
    }

    fun updateTitle(title: String) {
        _createOrModifyAppointmentReq.value?.title = title
    }

    fun updateReason(reason: String) {
        _createOrModifyAppointmentReq.value?.reason = reason
    }

    fun updateSelectedMonk(selectedMonkName: String) {
        monkMapping[selectedMonkName]?.let {
            _createOrModifyAppointmentReq.value?.selectedMonkId = it.id!!
            _createOrModifyAppointmentReq.value?.selectedMonk = it
        }?: run {
            _createOrModifyAppointmentReq.value?.selectedMonkId = "any"
            _createOrModifyAppointmentReq.value?.selectedMonk = null
        }
    }

    fun updateAppointmentType(appointmentType: String) {
        when(appointmentType) {
            AppointmentRequestType.REMOTE.name -> {
                _createOrModifyAppointmentReq.value?.appointmentReqType = AppointmentRequestType.REMOTE
            }
            AppointmentRequestType.ON_SITE.name -> {
                _createOrModifyAppointmentReq.value?.appointmentReqType = AppointmentRequestType.ON_SITE
            }
        }
    }

    fun getInitialSelectedMonk(): String {
        return _createOrModifyAppointmentReq.value?.selectedMonk?.let {
            "Bhanthe ${it.lastName}"
        }?: run {
            "any"
        }
    }

    fun createNewAppointmentRequest() {
        Session.user?.let {
            if (isValidAppointmentReqForm()) {
                _appointmentReqUiStatus.value = AppointmentCreateOrModifyUiStatus.PENDING
                viewModelScope.launch {
                    when (val response = appointmentRemoteRepo
                        .createAppointmentRequest(_createOrModifyAppointmentReq.value!!)) {
                        is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                            _appointmentReqUiStatus.value =
                                AppointmentCreateOrModifyUiStatus.FAILURE
                        }

                        is ResultWrapper.Success -> {
                            response.value.body?.let {
                                _appointmentReqUiStatus.value =
                                    AppointmentCreateOrModifyUiStatus.SUCCESS
                            }
                        }
                    }
                }
            } else {
                _appointmentReqUiStatus.value = AppointmentCreateOrModifyUiStatus.EMPTY_FIELD
            }
        }
    }

    fun updateAppointmentRequest() {
        Session.user?.let {
            _appointmentReqUiStatus.value = AppointmentCreateOrModifyUiStatus.PENDING
            viewModelScope.launch {
                when (val response = appointmentRemoteRepo
                    .updateAppointmentRequest(_createOrModifyAppointmentReq.value!!)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _appointmentReqUiStatus.value = AppointmentCreateOrModifyUiStatus.FAILURE
                    }
                    is ResultWrapper.Success -> {
                        response.value.body?.let {
                            _appointmentReqUiStatus.value = AppointmentCreateOrModifyUiStatus.SUCCESS
                        }
                    }
                }
            }
        }
    }

    private fun isValidAppointmentReqForm(): Boolean {
        return !(_createOrModifyAppointmentReq.value?.title == null
                || _createOrModifyAppointmentReq.value?.title?.isEmpty() == true
                || _createOrModifyAppointmentReq.value?.reason == null
                || _createOrModifyAppointmentReq.value?.reason?.isEmpty() == true)
    }
}