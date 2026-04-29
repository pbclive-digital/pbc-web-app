package com.kavi.pbc.web.auth.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.auth.data.repository.remote.AuthRemoteRepository
import com.kavi.pbc.web.auth.retrieveCurrentUser
import com.kavi.pbc.web.data.email.EmailGroupHeading
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.network.session.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel: ViewModel() {

    val authRemoteRepository = AuthRemoteRepository()

    private val _userProfile = MutableStateFlow(User(email = ""))
    val userProfile: StateFlow<User> = _userProfile

    private val _emailGroupHeadings = MutableStateFlow<List<EmailGroupHeading>>(mutableListOf())
    val emailGroupHeadings: StateFlow<List<EmailGroupHeading>> = _emailGroupHeadings

    fun fetchCurrentUser() {
        Session.user?.let { currentUser ->
            _userProfile.value = currentUser
        }?: run {
            retrieveCurrentUser { user ->
                user?.id?.let { userId ->
                    viewModelScope.launch {
                        when(val response = authRemoteRepository.getUser(userId = userId)) {
                            is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                                // Nothing to do
                            }
                            is ResultWrapper.Success -> {
                                response.value.body?.let {
                                    _userProfile.value = it
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun fetchUserEmailGroups() {
        viewModelScope.launch {
            when(val response = authRemoteRepository.getUserEmailGroupsByEmail(email = _userProfile.value.email)) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    // Nothing to do
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _emailGroupHeadings.value = it
                    }
                }
            }
        }
    }
}