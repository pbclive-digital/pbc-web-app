package com.kavi.pbc.web.event.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.event.data.model.EventListUiState
import com.kavi.pbc.web.event.data.repository.remote.EventRemoteRepository
import com.kavi.pbc.web.network.model.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventListViewModel: ViewModel() {

    val eventRemoteRepository = EventRemoteRepository()

    private val _upcomingEventList = MutableStateFlow<List<Event>>(mutableListOf())
    val upcomingEventList: StateFlow<List<Event>> = _upcomingEventList

    private val _upcomingEventListUiState = MutableStateFlow(EventListUiState.NONE)
    val upcomingEventListUiState: StateFlow<EventListUiState> = _upcomingEventListUiState

    private val _pastEventList = MutableStateFlow<List<Event>>(mutableListOf())
    val pastEventList: StateFlow<List<Event>> = _pastEventList

    private val _pastEventListUiState = MutableStateFlow(EventListUiState.NONE)
    val pastEventListUiState: StateFlow<EventListUiState> = _pastEventListUiState

    private val _recurringEventList = MutableStateFlow<List<Event>>(mutableListOf())
    val recurringEventList: StateFlow<List<Event>> = _recurringEventList

    private val _recurringEventListUiState = MutableStateFlow(EventListUiState.NONE)
    val recurringEventListUiState: StateFlow<EventListUiState> = _recurringEventListUiState

    fun fetchUpcomingEvents() {
        _upcomingEventListUiState.value = EventListUiState.PENDING
        viewModelScope.launch {
            when(val response = eventRemoteRepository.getUpcomingEvents()) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    _upcomingEventListUiState.value = EventListUiState.FAILURE
                }
                is ResultWrapper.Success -> {
                    _upcomingEventListUiState.value = EventListUiState.SUCCESS
                    response.value.body?.let {
                        _upcomingEventList.value = it
                    }
                }
            }
        }
    }

    fun fetchRecurringEvents() {
        viewModelScope.launch {
            _recurringEventListUiState.value = EventListUiState.PENDING
            when(val response = eventRemoteRepository.getRecurringEvents()) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    _recurringEventListUiState.value = EventListUiState.FAILURE
                }
                is ResultWrapper.Success -> {
                    _recurringEventListUiState.value = EventListUiState.SUCCESS
                    response.value.body?.let {
                        _recurringEventList.value = it
                    }
                }
            }
        }
    }

    fun fetchPastEvents() {
        viewModelScope.launch {
            _pastEventListUiState.value = EventListUiState.PENDING
            when(val response = eventRemoteRepository.getPastEvents()) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    _pastEventListUiState.value = EventListUiState.FAILURE
                }
                is ResultWrapper.Success -> {
                    _pastEventListUiState.value = EventListUiState.SUCCESS
                    response.value.body?.let {
                        _pastEventList.value = it
                    }
                }
            }
        }
    }

    fun fetchPastEventsWithLimit() {
        viewModelScope.launch {
            _pastEventListUiState.value = EventListUiState.PENDING
            when(val response = eventRemoteRepository.getPastEventsWithLimit(limit = 5)) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    _pastEventListUiState.value = EventListUiState.FAILURE
                }
                is ResultWrapper.Success -> {
                    _pastEventListUiState.value = EventListUiState.SUCCESS
                    response.value.body?.let {
                        _pastEventList.value = it
                    }
                }
            }
        }
    }
}