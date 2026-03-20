package com.kavi.pbc.web.appointment.ui.create.appointment

import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.appointment.data.model.AppointmentCreateOrModifyUiStatus
import com.kavi.pbc.web.appointment.data.repository.local.AppointmentLocalRepository
import com.kavi.pbc.web.appointment.data.repository.remote.AppointmentRemoteRepository
import com.kavi.pbc.web.data.appointment.Appointment
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.data.util.DateTimeUtil
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.network.session.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.collections.set

class AppointmentCreateOrModifyViewModel: ViewModel() {

    private val appointmentRemoteRepo = AppointmentRemoteRepository()
    private val appointmentLocalRepo = AppointmentLocalRepository()

    // Make this news is nullable, because need to clear the appointment object when creation or modify complete
    private val _createOrModifyAppointment: MutableStateFlow<Appointment?> = MutableStateFlow(Appointment(
        user = User(email = "")
    ))
    val createOrModifyAppointment: StateFlow<Appointment?> = _createOrModifyAppointment

    private val _residenceMonkList = MutableStateFlow<List<String>>(mutableListOf())
    val residenceMonkList: StateFlow<List<String>> = _residenceMonkList

    private val _appointmentUiStatus = MutableStateFlow(AppointmentCreateOrModifyUiStatus.NONE)
    val appointmentUiStatus: StateFlow<AppointmentCreateOrModifyUiStatus> = _appointmentUiStatus

    val monkMapping = mutableMapOf<String, User>()

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

    fun setModifyAppointment(appointmentReq: Appointment) {
        _createOrModifyAppointment.value = appointmentReq
    }

    fun initiateNewAppointment(selectedMonk: User? = null) {
        Session.user?.let {
            _createOrModifyAppointment.value = Appointment(
                userId = it.id!!,
                user = it,
                selectedMonk = selectedMonk,
                selectedMonkId = selectedMonk?.id?: ""
            )
        }
    }

    fun getInitialSelectedMonk(): String {
        return _createOrModifyAppointment.value?.selectedMonk?.let {
            "Bhanthe ${it.lastName}"
        }?: run {
            "none"
        }
    }

    fun getInitialAppointmentDate(): String {
        return if (_createOrModifyAppointment.value!!.date.toInt() == 0)
            "SELECT DATE"
        else
            _createOrModifyAppointment.value!!.getFormatDate()
    }

    fun getInitialTime(): String {
        return _createOrModifyAppointment.value!!.time.ifEmpty {
            "TIME"
        }
    }

    fun updateTitle(title: String) {
        _createOrModifyAppointment.value?.title = title
    }

    fun updateReason(reason: String) {
        _createOrModifyAppointment.value?.reason = reason
    }

    fun updateSelectedMonk(selectedMonkName: String) {
        monkMapping[selectedMonkName]?.let {
            _createOrModifyAppointment.value?.selectedMonkId = it.id!!
            _createOrModifyAppointment.value?.selectedMonk = it
        }?: run {
            _createOrModifyAppointment.value?.selectedMonkId = "none"
            _createOrModifyAppointment.value?.selectedMonk = null
        }
    }

    fun updateDate(date: Long?) {
        date?.let {
            _createOrModifyAppointment.value?.date = it
        }
    }

    fun updateTime(time: String) {
        _createOrModifyAppointment.value?.time = time
    }

    fun clearAppointment() {
        _createOrModifyAppointment.value = null
    }

    fun revokeAppointmentUiStatus() {
        _appointmentUiStatus.value = AppointmentCreateOrModifyUiStatus.NONE
    }

    fun formatDate(selectedDateMils: Long?): String {
        return selectedDateMils?.let {
            DateTimeUtil.formatDate(it)
        }?: run {
            "Select Event Date".uppercase()
        }
    }

    fun formatTime(hour: Int, minute: Int): String {
        // Ensure the values stay within bounds (Optional safety check)
        val h = hour.coerceIn(0, 23)
        val m = minute.coerceIn(0, 59)

        // padStart ensures there are always 2 digits, using '0' as a filler
        val hourString = h.toString().padStart(2, '0')
        val minuteString = m.toString().padStart(2, '0')

        return "$hourString:$minuteString"
    }

    fun createNewAppointment(appointmentReqId: String? = null) {
        Session.user?.let {
            _appointmentUiStatus.value = AppointmentCreateOrModifyUiStatus.PENDING
            viewModelScope.launch {
                when (val response = appointmentRemoteRepo
                    .createAppointment(_createOrModifyAppointment.value!!)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _appointmentUiStatus.value = AppointmentCreateOrModifyUiStatus.FAILURE
                    }
                    is ResultWrapper.Success -> {
                        response.value.body?.let {
                            _appointmentUiStatus.value = AppointmentCreateOrModifyUiStatus.SUCCESS

                            appointmentReqId?.let {
                                deleteAppointmentRequest(it)
                            }
                        }
                    }
                }
            }
        }
    }

    fun updateAppointment() {
        Session.user?.let {
            _appointmentUiStatus.value = AppointmentCreateOrModifyUiStatus.PENDING
            viewModelScope.launch {
                when (val response = appointmentRemoteRepo
                    .updateAppointment(_createOrModifyAppointment.value!!)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _appointmentUiStatus.value = AppointmentCreateOrModifyUiStatus.FAILURE
                    }
                    is ResultWrapper.Success -> {
                        response.value.body?.let {
                            _appointmentUiStatus.value = AppointmentCreateOrModifyUiStatus.SUCCESS
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
                    //fetchAppointmentRequests()
                    //checkAppointmentReqCreateEligibility()
                }
            }
        }
    }
}