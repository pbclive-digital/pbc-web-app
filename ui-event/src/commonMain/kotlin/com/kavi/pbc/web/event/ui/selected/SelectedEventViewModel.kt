package com.kavi.pbc.web.event.ui.selected

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.event.potluck.EventPotluck
import com.kavi.pbc.web.data.event.potluck.EventPotluckContributor
import com.kavi.pbc.web.data.event.potluck.EventPotluckItem
import com.kavi.pbc.web.data.event.register.EventRegistration
import com.kavi.pbc.web.data.event.register.EventRegistrationItem
import com.kavi.pbc.web.data.event.signup.EventSignUpSheetContributor
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

    private val _eventPotluckData = MutableStateFlow(EventPotluck("", mutableListOf()))
    val eventPotluckData: StateFlow<EventPotluck> = _eventPotluckData

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

                        if (_selectedEvent.value.potluckAvailable) {
                            fetchPotluckDetails()
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

    fun isCurrentUserSignUpToSignUpSheet(sheetId: String): Boolean {
        val filteredSignUpSheetList = _eventSignUpSheetData.value.signUpSheetItemList.filter { it.sheetId == sheetId }
        return if (filteredSignUpSheetList.isNotEmpty()) {
            val selectedSheet = filteredSignUpSheetList[0]
            val filtered = selectedSheet.contributorList.filter { it.contributorId == Session.user?.id }
            filtered.isNotEmpty()
        } else {
            false
        }
    }

    fun remainingSignUpCountInSignUpSheet(sheetId: String): Int {
        val filteredSignUpSheetList = _eventSignUpSheetData.value.signUpSheetItemList.filter { it.sheetId == sheetId }
        var remainingCount = 0
        if (filteredSignUpSheetList.isNotEmpty()) {
            val selectedSignUpSheet = filteredSignUpSheetList[0]
            remainingCount = selectedSignUpSheet.availableCount - selectedSignUpSheet.contributorList.size
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

    fun signUpToSheet(sheetId: String, onComplete: () -> Unit) {
        Session.user?.let { sessionUser ->
            val sheetContributor = EventSignUpSheetContributor(
                sessionUser.id!!,
                "${sessionUser.firstName!!} ${sessionUser.lastName!!}",
                sessionUser.phoneNumber
            )

            viewModelScope.launch {
                when(val response = eventRemoteRepository.signUpToSelectedSignUpSheet(
                    _selectedEvent.value.id!!, sheetId = sheetId, contributor = sheetContributor
                )) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _eventActionUiState.value = EventActionUiState.FAILURE
                        onComplete.invoke()
                    }
                    is ResultWrapper.Success -> {
                        _eventActionUiState.value = EventActionUiState.SUCCESS
                        onComplete.invoke()
                        response.value.body?.let {
                            _eventSignUpSheetData.value = it
                        }
                    }
                }
            }
        }
    }

    fun signOutFromSheet(sheetId: String, onComplete: () -> Unit) {
        Session.user?.let { sessionUser ->
            viewModelScope.launch {
                when(val response = eventRemoteRepository.signOutFromSelectedSignUpSheet(
                    _selectedEvent.value.id!!, sheetId = sheetId, contributorId = sessionUser.id!!)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _eventActionUiState.value = EventActionUiState.FAILURE
                        onComplete.invoke()
                    }
                    is ResultWrapper.Success -> {
                        _eventActionUiState.value = EventActionUiState.SUCCESS
                        onComplete.invoke()
                        response.value.body?.let {
                            _eventSignUpSheetData.value = it
                        }
                    }
                }
            }
        }
    }

    fun checkedCurrentUserContribution(potluckItem: EventPotluckItem): Int {
        Session.user?.let { currentUser ->
            val contribution = potluckItem.contributorList.filter { contributor -> contributor.contributorId == currentUser.id }
            return contribution.size
        }
        return 0
    }

    fun signUpForPotluckItem(potluckItem: EventPotluckItem) {
        Session.user?.let {
            val eventPotluckContributor = EventPotluckContributor(
                contributorId = it.id!!,
                contributorName = "${it.firstName} ${it.lastName}",
                contributorContactNumber = it.phoneNumber ?: run { "" }
            )

            viewModelScope.launch {
                _eventActionUiState.value = EventActionUiState.PENDING
                when(val response = eventRemoteRepository
                    .signUpToPotluck(eventId =_selectedEvent.value.id!!, potluckItemId = potluckItem.itemId, eventPotluckContributor)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _eventActionUiState.value = EventActionUiState.FAILURE
                    }
                    is ResultWrapper.Success -> {
                        _eventActionUiState.value = EventActionUiState.SUCCESS
                        response.value.body?.let { updatedEventPotluck ->
                            _eventPotluckData.value = updatedEventPotluck
                        }
                    }
                }
            }
        }
    }

    fun signOutFromPotluckItem(potluckItem: EventPotluckItem) {
        Session.user?.let {
            viewModelScope.launch {
                _eventActionUiState.value = EventActionUiState.PENDING
                when(val response = eventRemoteRepository
                    .signOutFromPotluck(eventId =_selectedEvent.value.id!!, potluckItemId = potluckItem.itemId, contributorId = it.id!!)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _eventActionUiState.value = EventActionUiState.FAILURE
                    }
                    is ResultWrapper.Success -> {
                        _eventActionUiState.value = EventActionUiState.SUCCESS
                        response.value.body?.let { updatedEventPotluck ->
                            _eventPotluckData.value = updatedEventPotluck
                        }
                    }
                }
            }
        }
    }

    private fun fetchRegistrationDetails() {
        viewModelScope.launch {
            when(val response = eventRemoteRepository.getEventRegistration(_selectedEvent.value.id!!)) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _eventRegistrationData.value = it
                    }
                }
            }
        }
    }

    private fun fetchPotluckDetails() {
        viewModelScope.launch {
            when(val response = eventRemoteRepository.getEventPotluck(_selectedEvent.value.id!!)) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _eventPotluckData.value = it
                    }
                }
            }
        }
    }

    private fun fetchSignUpSheetDetails() {
        viewModelScope.launch {
            when(val response = eventRemoteRepository.getSignUpSheetList(_selectedEvent.value.id!!)) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _eventSignUpSheetData.value = it
                    }
                }
            }
        }
    }
}