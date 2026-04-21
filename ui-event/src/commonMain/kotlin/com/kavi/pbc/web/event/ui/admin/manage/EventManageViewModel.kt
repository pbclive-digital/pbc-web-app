package com.kavi.pbc.web.event.ui.admin.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.event.EventType
import com.kavi.pbc.web.event.data.model.EventManageMode
import com.kavi.pbc.web.event.data.repository.remote.EventRemoteRepository
import com.kavi.pbc.web.network.model.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventManageViewModel: ViewModel() {

    val eventRemoteRepository = EventRemoteRepository()

    private val _draftEventList = MutableStateFlow<List<Event>>(mutableListOf())
    val draftEventList: StateFlow<List<Event>> = _draftEventList

    private val _recurringEventList = MutableStateFlow<List<Event>>(mutableListOf())
    val recurringEventList: StateFlow<List<Event>> = _recurringEventList

    private val _activeEventList = MutableStateFlow<List<Event>>(mutableListOf())
    val activeEventList: StateFlow<List<Event>> = _activeEventList

    fun fetchDraftEvents() {
        viewModelScope.launch {
            when(val response = eventRemoteRepository.getDraftEvents()) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {

                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _draftEventList.value = it
                    }
                }
            }
        }
    }

    fun fetchRecurringEvents() {
        viewModelScope.launch {
            when(val response = eventRemoteRepository.getRecurringEvents()) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {

                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _recurringEventList.value = it
                    }
                }
            }
        }
    }

    fun fetchActiveEvents() {
        viewModelScope.launch {
            when(val response = eventRemoteRepository.getUpcomingEvents()) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {

                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _activeEventList.value = it
                    }
                }
            }
        }
    }

    fun publishDraftEvent(eventId: String) {
        val eventFilter = _draftEventList.value.filter { it.id == eventId }
        if (eventFilter.isNotEmpty() && eventFilter.size == 1) {
            viewModelScope.launch {
                when (val response = eventRemoteRepository.publishDraftEvent(
                    eventId = eventId,
                    event = eventFilter[0]
                )) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        // Nothing to do as per now.
                    }
                    is ResultWrapper.Success -> {
                        response.value.body?.let { updatedEvent ->
                            _draftEventList.value = _draftEventList.value
                                .filterNot { it.id == eventId }
                                .toMutableList()

                            when(updatedEvent.eventType) {
                                EventType.RECURRING -> {
                                    _recurringEventList.update { currentList ->
                                        (currentList + updatedEvent) as MutableList<Event>
                                    }
                                }
                                else -> {
                                    _activeEventList.update { currentList ->
                                        (currentList + updatedEvent) as MutableList<Event>
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun deleteEvent(eventId: String, eventManageMode: EventManageMode) {
        if (eventId.isNotEmpty()) {
            viewModelScope.launch {
                when (eventRemoteRepository.deleteEvent(eventId = eventId)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        // Nothing to do as per now.
                    }
                    is ResultWrapper.Success -> {
                        when(eventManageMode) {
                            EventManageMode.DRAFT -> {
                                _draftEventList.value = _draftEventList.value
                                    .filterNot { it.id == eventId }
                                    .toMutableList()
                            }
                            EventManageMode.RECURRING -> {
                                _recurringEventList.value = _recurringEventList.value
                                    .filterNot { it.id == eventId }
                                    .toMutableList()
                            }
                            EventManageMode.ACTIVE -> {
                                _activeEventList.value = _activeEventList.value
                                    .filterNot { it.id == eventId }
                                    .toMutableList()
                            }
                            EventManageMode.UNSELECTED -> {
                                // Do Nothing
                            }
                        }
                    }
                }
            }
        }
    }

    fun downloadEventRegistrationList(eventId: String, onLinkAvailable: (urlPath: String) -> Unit) {
        viewModelScope.launch {
            when (val response = eventRemoteRepository.getEventRegistrationDownloadLink(eventId = eventId)) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    // Nothing to do as per now.
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let { downloadData ->
                        onLinkAvailable.invoke(downloadData.downloadLink)
                    }
                }
            }
        }
    }

    fun downloadEventPotluckContribution(eventId: String, onLinkAvailable: (urlPath: String) -> Unit) {
        viewModelScope.launch {
            when (val response = eventRemoteRepository.getEventPotluckDownloadLink(eventId = eventId)) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    // Nothing to do as per now.
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let { downloadData ->
                        onLinkAvailable.invoke(downloadData.downloadLink)
                    }
                }
            }
        }
    }

    fun downloadEventSignUpSheetContribution(eventId: String, sheetId: String, onLinkAvailable: (urlPath: String) -> Unit) {
        viewModelScope.launch {
            when (val response = eventRemoteRepository.getEventSignUpSheetDownloadLink(eventId = eventId, sheetId = sheetId)) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    // Nothing to do as per now.
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let { downloadData ->
                        onLinkAvailable.invoke(downloadData.downloadLink)
                    }
                }
            }
        }
    }
}