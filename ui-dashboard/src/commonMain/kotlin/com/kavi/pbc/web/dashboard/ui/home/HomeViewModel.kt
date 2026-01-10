package com.kavi.pbc.web.dashboard.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.dashboard.data.repository.remote.DashboardRemoteRepository
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.quote.Quote
import com.kavi.pbc.web.network.model.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    val dashboardRemoteRepository = DashboardRemoteRepository()

    private val _dashboardQuoteList = MutableStateFlow<List<Quote>>(mutableListOf())
    val dashboardQuoteList: StateFlow<List<Quote>> = _dashboardQuoteList
    private val _dashboardEventList = MutableStateFlow<List<Event>>(mutableListOf())
    val dashboardEventList: StateFlow<List<Event>> = _dashboardEventList

    fun fetchDashboardQuotesFromRemote() {
        viewModelScope.launch {
            when(val response = dashboardRemoteRepository.fetchDashboardDailyQuotes()) {
                is ResultWrapper.NetworkError -> {}
                is ResultWrapper.HttpError -> {}
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _dashboardQuoteList.value = it
                        //storeRetrievedQuotes(it)
                    }
                }
            }
        }
    }

    fun fetchDashboardEvents() {
        viewModelScope.launch {
            when(val response = dashboardRemoteRepository.fetchDashboardEvents()) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    //_splashUiState.value = SplashUiState.ON_ERROR
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _dashboardEventList.value = it
                    }
                }
            }
        }
    }
}