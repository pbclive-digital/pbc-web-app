package com.kavi.pbc.web.users.ui.users.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.news.News
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.data.user.UserRoleUpdateReq
import com.kavi.pbc.web.data.user.UserType
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.users.data.model.UserDeleteUiState
import com.kavi.pbc.web.users.data.model.UserRoleUpdateUiState
import com.kavi.pbc.web.users.data.repository.remote.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserManageViewModel: ViewModel() {

    val userRemoteRepository = UserRepository()

    private val _adminUserList = MutableStateFlow<List<User>>(mutableListOf())
    val adminUserList: StateFlow<List<User>> = _adminUserList

    private val _residentMonkList = MutableStateFlow<List<User>>(mutableListOf())
    val residentMonksList: StateFlow<List<User>> = _residentMonkList

    private val _consumerUserList = MutableStateFlow<List<User>>(mutableListOf())

    private val _letterGroupedConsumerList = MutableStateFlow(mapOf<Char, List<User>>())
    val letterGroupedConsumerList: StateFlow<Map<Char, List<User>>> = _letterGroupedConsumerList

    private val _userDeleteUiState = MutableStateFlow(UserDeleteUiState.NONE)
    val userDeleteUiState: StateFlow<UserDeleteUiState> = _userDeleteUiState

    private val _userRoleUpdateUiState = MutableStateFlow(UserRoleUpdateUiState.NONE)
    val userRoleUpdateUiState: StateFlow<UserRoleUpdateUiState> = _userRoleUpdateUiState

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

    fun fetchResidentMonks() {
        viewModelScope.launch {
            when(val response = userRemoteRepository.getResidentMonkList()) {
                is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                    // Do nothing for now
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _residentMonkList.value = it
                        categorizeUserList()
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

    fun modifyUserRole(newUserRole: String, residentMonkFlag: Boolean, user: User) {
        val userRoleUpdate = UserRoleUpdateReq(
            newRole = newUserRole,
            residentMonkFlag = residentMonkFlag,
            user = user
        )

        viewModelScope.launch {
            _userRoleUpdateUiState.value = UserRoleUpdateUiState.PENDING
            when(val response = userRemoteRepository.updateUserRole(userRoleUpdateReq = userRoleUpdate)) {
                is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                    _userRoleUpdateUiState.value = UserRoleUpdateUiState.FAILURE
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _userRoleUpdateUiState.value = UserRoleUpdateUiState.SUCCESS
                        fetchAdmins()
                        fetchResidentMonks()
                    }
                }
            }
        }
    }

    fun deleteConsumerUser(deletingUserId: String) {
        viewModelScope.launch {
            _userDeleteUiState.value = UserDeleteUiState.PENDING
            when(val response = userRemoteRepository.deleteConsumerUser(userId = deletingUserId)) {
                is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                    _userDeleteUiState.value = UserDeleteUiState.FAILURE
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _userDeleteUiState.value = UserDeleteUiState.SUCCESS
                        _consumerUserList.value = _consumerUserList.value.filterNot { it.id == deletingUserId }
                        categorizeUserList()
                    }
                }
            }
        }
    }

    fun revokeAllUiStates() {
        _userDeleteUiState.value = UserDeleteUiState.NONE
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