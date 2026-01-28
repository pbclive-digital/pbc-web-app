package com.kavi.pbc.web.event.ui.selected

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.event.register.EventRegistration
import com.kavi.pbc.web.data.event.register.EventRegistrationItem
import com.kavi.pbc.web.data.event.signup.EventSignUpSheetList
import com.kavi.pbc.web.event.data.model.EventActionUiState
import com.kavi.pbc.web.event.data.repository.remote.EventRemoteRepository
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.network.session.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SelectedEventViewModel: ViewModel() {
    val eventRemoteRepository = EventRemoteRepository()

    private val _selectedEvent = MutableStateFlow<Event>(Event())
    val selectedEvent: StateFlow<Event> = _selectedEvent

    private val _eventSignUpSheetData = MutableStateFlow(EventSignUpSheetList(""))
    val eventSignUpSheetData: StateFlow<EventSignUpSheetList> = _eventSignUpSheetData

    private val _eventRegistrationData = MutableStateFlow(EventRegistration("", 0))

    private val _eventActionUiState = MutableStateFlow(EventActionUiState.NONE)
    val eventActionUiState: StateFlow<EventActionUiState> = _eventActionUiState

    fun fetchEventDetails(eventId: String) {
        viewModelScope.launch {
            when(val response = eventRemoteRepository.getEventDetails(eventId = eventId)) {
                is ResultWrapper.NetworkError -> {}
                is ResultWrapper.HttpError -> {}
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _selectedEvent.value = it

                        if (_selectedEvent.value.signUpSheetAvailable) {
                            fetchSignUpSheetDetails()
                        }

                        if (_selectedEvent.value.registrationRequired) {
                            fetchRegistrationDetails()
                        }
                    }
                }
            }
        }
    }

    fun isCurrentUserRegistered(): Boolean {
        return if (_selectedEvent.value.registrationRequired) {
            val filtered = _eventRegistrationData.value.registrationList.filter { it.participantUserId == Session.user?.id }
            filtered.isNotEmpty()
        } else {
            false
        }
    }

    fun remainingSeatCountAvailable(): Int {
        var remainingCount = 0
        if (_selectedEvent.value.registrationRequired) {
            _selectedEvent.value.openSeatCount?.let {
                remainingCount = it - _eventRegistrationData.value.registrationList.size
            }
        }

        return remainingCount
    }

    fun registerToEvent() {
        Session.user?.let { sessionUser ->
            val eventRegistrationItem = EventRegistrationItem(
                participantUserId = sessionUser.id!!,
                participantName = "${sessionUser.firstName!!} ${sessionUser.lastName!!}",
                participantAddress = sessionUser.address,
                participantContactNumber = sessionUser.phoneNumber
            )

            viewModelScope.launch {
                _eventActionUiState.value = EventActionUiState.PENDING
                when(val response = eventRemoteRepository.registerToEvent(_selectedEvent.value.id!!, eventRegistrationItem = eventRegistrationItem)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _eventActionUiState.value = EventActionUiState.FAILURE
                    }
                    is ResultWrapper.Success -> {
                        _eventActionUiState.value = EventActionUiState.SUCCESS
                        response.value.body?.let {
                            _eventRegistrationData.value = it
                        }
                    }
                }
            }
        }
    }

    fun unregisterFromEvent() {
        Session.user?.let { sessionUser ->
            viewModelScope.launch {
                _eventActionUiState.value = EventActionUiState.PENDING
                when(val response = eventRemoteRepository
                    .unregisterFromEvent(_selectedEvent.value.id!!, userId = sessionUser.id!!)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _eventActionUiState.value = EventActionUiState.FAILURE
                    }
                    is ResultWrapper.Success -> {
                        _eventActionUiState.value = EventActionUiState.SUCCESS
                        response.value.body?.let {
                            _eventRegistrationData.value = it
                        }
                    }
                }
            }
        }
    }

    private fun fetchRegistrationDetails() {
        viewModelScope.launch {
            when(val response = eventRemoteRepository.getEventRegistration(_selectedEvent.value.id!!)) {
                is ResultWrapper.NetworkError -> {}
                is ResultWrapper.HttpError -> {}
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _eventRegistrationData.value = it
                    }
                }
            }
        }
    }

    private fun fetchSignUpSheetDetails() {
        viewModelScope.launch {
            when(val response = eventRemoteRepository.getSignUpSheetList(_selectedEvent.value.id!!)) {
                is ResultWrapper.NetworkError -> {}
                is ResultWrapper.HttpError -> {}
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _eventSignUpSheetData.value = it
                    }
                }
            }
        }
    }
}