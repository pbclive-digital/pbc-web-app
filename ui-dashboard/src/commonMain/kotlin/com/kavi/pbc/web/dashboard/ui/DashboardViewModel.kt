package com.kavi.pbc.web.dashboard.ui

import androidx.lifecycle.ViewModel
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.data.user.UserType
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel: ViewModel() {

    private var _isAdminUser = MutableStateFlow(false)
    val isAdminUser: StateFlow<Boolean> = _isAdminUser

    private var _user = MutableStateFlow(User(email = ""))
    val user: StateFlow<User> = _user

    fun fetchUser() {
        if (!isValidUser()) {
            ContractServiceLocator.locate(AuthContract::class).retrieveUser(onSuccess = { user ->
                _user.value = user
                if (user.userType == UserType.ADMIN || user.residentMonk) {
                    _isAdminUser.value = true
                }
            }, onFailure = {
                _isAdminUser.value = false
            })
        }
    }

    private fun isValidUser(): Boolean {
        return _user.value.email.isBlank() && _user.value.id != null
    }
}