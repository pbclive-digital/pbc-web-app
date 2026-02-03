package com.kavi.pbc.web.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.datastore.AppLocalStore
import com.kavi.pbc.web.datastore.DataKey
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import com.kavi.pbc.web.splash.data.model.SplashUiState
import com.kavi.pbc.web.splash.data.repository.remote.SplashRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {

    private val _splashUiState = MutableStateFlow(SplashUiState.NONE)
    val splashUiState: StateFlow<SplashUiState> = _splashUiState

    val splashViewModel = SplashRemoteRepository()

    fun fetchConfig() {
        viewModelScope.launch {
            when(val response = splashViewModel.fetchConfig()) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    _splashUiState.value = SplashUiState.ON_ERROR
                }
                is ResultWrapper.Success -> {
                    // Check authentication status
                    ContractServiceLocator.locate(AuthContract::class).retrieveCurrentAuthStatus { authStatus ->
                        AppLocalStore.shared.storeValue(DataKey.APP_USER_AUTH_STATUS, authStatus)
                        _splashUiState.value = SplashUiState.ON_DASHBOARD_NAV
                    }
                }
            }
        }
    }
}