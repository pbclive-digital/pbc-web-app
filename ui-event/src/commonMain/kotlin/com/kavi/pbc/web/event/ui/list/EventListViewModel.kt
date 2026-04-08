package com.kavi.pbc.web.event.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.event.data.repository.remote.EventRemoteRepository
import com.kavi.pbc.web.network.model.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventListViewModel: ViewModel() {

    val eventRemoteRepository = EventRemoteRepository()

    private val _upcomingEventList = MutableStateFlow<List<Event>>(mutableListOf())
    val upcomingEventList: StateFlow<List<Event>> = _upcomingEventList

    private val _pastEventList = MutableStateFlow<List<Event>>(mutableListOf())
    val pastEventList: StateFlow<List<Event>> = _pastEventList

    private val _recurringEventList = MutableStateFlow<List<Event>>(mutableListOf())
    val recurringEventList: StateFlow<List<Event>> = _recurringEventList

    fun fetchUpcomingEvents() {
        viewModelScope.launch {
            when(val response = eventRemoteRepository.getUpcomingEvents()) {
                is ResultWrapper.NetworkError -> {}
                is ResultWrapper.HttpError -> {}
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _upcomingEventList.value = it
                    }
                }
            }
        }
    }

    fun fetchRecurringEvents() {
        viewModelScope.launch {
            when(val response = eventRemoteRepository.getRecurringEvents()) {
                is ResultWrapper.NetworkError -> {}
                is ResultWrapper.HttpError -> {}
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _recurringEventList.value = it
                    }
                }
            }
        }
    }

    fun fetchPastEvents() {
        viewModelScope.launch {
            when(val response = eventRemoteRepository.getPastEvents()) {
                is ResultWrapper.NetworkError -> {}
                is ResultWrapper.HttpError -> {}
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _pastEventList.value = it
                    }
                }
            }
        }
    }

    fun fetchPastEventsWithLimit() {
        viewModelScope.launch {
            when(val response = eventRemoteRepository.getPastEventsWithLimit(limit = 5)) {
                is ResultWrapper.NetworkError -> {}
                is ResultWrapper.HttpError -> {}
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _pastEventList.value = it
                    }
                }
            }
        }
    }
}