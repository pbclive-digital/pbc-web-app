package com.kavi.pbc.web.dashboard.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.dashboard.data.repository.remote.DashboardRemoteRepository
import com.kavi.pbc.web.network.model.ResultWrapper
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    val dashboardRemoteRepository = DashboardRemoteRepository()

    fun fetchDashboardEvents() {
        viewModelScope.launch {
            when(val response = dashboardRemoteRepository.fetchDashboardEvents()) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    //_splashUiState.value = SplashUiState.ON_ERROR
                }
                is ResultWrapper.Success -> {
                    //_splashUiState.value = SplashUiState.ON_DASHBOARD_NAV
                }
            }
        }
    }
}