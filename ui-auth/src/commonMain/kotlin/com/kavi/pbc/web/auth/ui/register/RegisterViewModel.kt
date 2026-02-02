package com.kavi.pbc.web.auth.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.auth.data.model.UserRegisterUiState
import com.kavi.pbc.web.auth.data.repository.remote.AuthRemoteRepository
import com.kavi.pbc.web.auth.retrieveCurrentUser
import com.kavi.pbc.web.data.auth.AuthToken
import com.kavi.pbc.web.data.auth.TokenStatus
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.network.session.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {

    private val authRemoteRepository = AuthRemoteRepository()
    private val _signedUser = MutableStateFlow(User(email = ""))
    val signedUser: StateFlow<User> = _signedUser

    private val _userRegisterUiState = MutableStateFlow(UserRegisterUiState.NONE)
    val userRegisterUiState: StateFlow<UserRegisterUiState> = _userRegisterUiState

    fun createUserFromFirebaseAuth() {
        retrieveCurrentUser { user ->
            user?.let {
                _signedUser.value = it
            }
        }
    }

    fun updateUserFirstName(firstName: String) {
        _signedUser.value.firstName = firstName
    }

    fun updateUserLastName(lastName: String) {
        _signedUser.value.lastName = lastName
    }

    fun updateUserPhoneNum(phoneNum: String) {
        _signedUser.value.phoneNumber = phoneNum
    }

    fun updateUserAddress(address: String) {
        _signedUser.value.address = address
    }

    fun registerNewUser() {
        viewModelScope.launch {
            _userRegisterUiState.value = UserRegisterUiState.PENDING
            when(authRemoteRepository.registerNewUser(_signedUser.value)) {
                is ResultWrapper.NetworkError -> {
                    _userRegisterUiState.value = UserRegisterUiState.FAILED
                }
                is ResultWrapper.HttpError -> {
                    _userRegisterUiState.value = UserRegisterUiState.FAILED
                }
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    Session.user = _signedUser.value
                    generateAuthToken()
                }
            }
        }
    }

    private fun generateAuthToken() {
        var authToken = AuthToken(
            email = _signedUser.value.email,
            userId = _signedUser.value.id,
            status = TokenStatus.VALID
        )

        viewModelScope.launch {
            when(val response = authRemoteRepository.createNewToken(authToken)) {
                is ResultWrapper.NetworkError -> {
                    Session.authToken = null
                    _userRegisterUiState.value = UserRegisterUiState.AUTH_FAILED
                }
                is ResultWrapper.HttpError -> {
                    Session.authToken = null
                    _userRegisterUiState.value = UserRegisterUiState.AUTH_FAILED
                }
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    authToken = response.value.body!!
                    Session.authToken = authToken
                    _userRegisterUiState.value = UserRegisterUiState.AUTHENTICATED
                }
            }
        }
    }
}