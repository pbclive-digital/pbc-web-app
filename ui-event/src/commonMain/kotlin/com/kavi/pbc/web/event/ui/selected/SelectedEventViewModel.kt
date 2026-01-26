package com.kavi.pbc.web.event.ui.selected

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.event.signup.EventSignUpSheetList
import com.kavi.pbc.web.event.data.repository.remote.EventRemoteRepository
import com.kavi.pbc.web.network.model.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SelectedEventViewModel: ViewModel() {
    val eventRemoteRepository = EventRemoteRepository()

    private val _selectedEvent = MutableStateFlow<Event>(Event())
    val selectedEvent: StateFlow<Event> = _selectedEvent

    private val _eventSignUpSheetData = MutableStateFlow(EventSignUpSheetList(""))
    val eventSignUpSheetData: StateFlow<EventSignUpSheetList> = _eventSignUpSheetData

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