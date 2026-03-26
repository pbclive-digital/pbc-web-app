package com.kavi.pbc.web.event.ui.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.event.data.repository.remote.EventRemoteRepository
import com.kavi.pbc.web.network.model.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventManageViewModel: ViewModel() {

    val eventRemoteRepository = EventRemoteRepository()

    private val _draftEventList = MutableStateFlow<List<Event>>(mutableListOf())
    val draftEventList: StateFlow<List<Event>> = _draftEventList

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
}