package com.kavi.pbc.web.auth.service.auth

import com.kavi.pbc.web.auth.data.repository.remote.AuthRemoteRepository
import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.network.session.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthServiceModel {
    private val authRemoteRepository = AuthRemoteRepository()

    fun fetchUserStatus(email: String, userId: String, onComplete: (authStatus: AppAuthStatus) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            when(val response = authRemoteRepository.getUserStatus(email = email, userId = userId)) {
                is ResultWrapper.NetworkError -> {}
                is ResultWrapper.HttpError -> {}
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let { status ->
                        when(status) {
                            "REGISTERED" -> requestAuthToken(email = email, userId = userId, onComplete = onComplete)
                            "UNREGISTERED" -> {
                                onComplete.invoke(AppAuthStatus.SIGN_UP_REQUIRED)
                            }
                        }
                    }
                }
            }
        }
    }

    fun fetchUser(userId: String, onComplete: (authStatus: AppAuthStatus) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            when(val response = authRemoteRepository.getUser(userId = userId)) {
                is ResultWrapper.NetworkError -> {}
                is ResultWrapper.HttpError -> {}
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let { user ->
                        Session.user = user
                        onComplete.invoke(AppAuthStatus.SIGN_IN)
                    }
                }
            }
        }
    }

    private fun requestAuthToken(email: String, userId: String, onComplete: (authStatus: AppAuthStatus) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            when(val response = authRemoteRepository.requestAuthToken(email = email, userId = userId)) {
                is ResultWrapper.NetworkError -> {}
                is ResultWrapper.HttpError -> {}
                is ResultWrapper.UnAuthError -> {

                }
                is ResultWrapper.Success -> {
                    response.value.body?.let { authToken ->
                        Session.authToken = authToken
                        fetchUser(userId = userId, onComplete = onComplete)
                    }
                }
            }
        }
    }
}