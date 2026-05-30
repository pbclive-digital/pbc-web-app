package com.kavi.pbc.web.users.ui.users.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.email.EmailItem
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.users.data.repository.remote.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserManageViewModel: ViewModel() {

    val userRemoteRepository = UserRepository()

    private val _adminUserList = MutableStateFlow<List<User>>(mutableListOf())
    val adminUserList: StateFlow<List<User>> = _adminUserList

    private val _consumerUserList = MutableStateFlow<List<User>>(mutableListOf())

    private val _letterGroupedConsumerList = MutableStateFlow(mapOf<Char, List<User>>())
    val letterGroupedConsumerList: StateFlow<Map<Char, List<User>>> = _letterGroupedConsumerList

    fun fetchAdmins() {
        viewModelScope.launch {
            when(val response = userRemoteRepository.getAdminUserList()) {
                is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                    // Do nothing for now
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _adminUserList.value = it
                    }
                }
            }
        }
    }

    fun fetchConsumers() {
        viewModelScope.launch {
            when(val response = userRemoteRepository.getConsumerUserList()) {
                is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                    // Do nothing for now
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _consumerUserList.value = it
                        categorizeUserList()
                    }
                }
            }
        }
    }

    private fun categorizeUserList() {
        _letterGroupedConsumerList.value = _consumerUserList.value
            .filter { it.firstName?.isNotEmpty() == true } // Filter items only name available
            .groupBy { it.firstName!!.first().lowercaseChar() } // Grouped by first letter of the first name
            .toList() // Convert to list
            .sortedBy { (key, _) -> key } // Sort by map key
            .toMap() // Convert back to map
    }
}