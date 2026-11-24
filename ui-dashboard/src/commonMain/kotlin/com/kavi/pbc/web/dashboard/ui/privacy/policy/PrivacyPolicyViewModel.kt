package com.kavi.pbc.web.dashboard.ui.privacy.policy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.dashboard.data.model.PolicyUiState
import com.kavi.pbc.web.dashboard.data.repository.remote.DashboardRemoteRepository
import com.kavi.pbc.web.data.privacy.PolicyItem
import com.kavi.pbc.web.data.privacy.PrivacyPolicy
import com.kavi.pbc.web.network.model.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PrivacyPolicyViewModel: ViewModel() {

    val dashboardRemoteRepository = DashboardRemoteRepository()

    private val _privacyPolicy = MutableStateFlow(PrivacyPolicy("", emptyList()))
    val privacyPolicy: StateFlow<PrivacyPolicy> = _privacyPolicy

    private val _policyFetchStatus = MutableStateFlow(PolicyUiState.NONE)
    val policyFetchStatus: StateFlow<PolicyUiState> = _policyFetchStatus

    fun fetchPrivacyPolicy() {
        _policyFetchStatus.value = PolicyUiState.PENDING
        viewModelScope.launch {
            when(val response = dashboardRemoteRepository.fetchPrivacyPolicy()) {
                is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                    _policyFetchStatus.value = PolicyUiState.FAILURE
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        if (it.policyItemList.isNotEmpty()) {
                            _policyFetchStatus.value = PolicyUiState.SUCCESS
                            _privacyPolicy.value = it
                        } else {
                            _policyFetchStatus.value = PolicyUiState.FAILURE
                        }
                    }
                }
            }
        }
    }
}