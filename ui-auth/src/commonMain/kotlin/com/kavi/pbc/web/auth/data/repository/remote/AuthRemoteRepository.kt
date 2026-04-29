package com.kavi.pbc.web.auth.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.auth.AuthToken
import com.kavi.pbc.web.data.email.EmailGroupHeading
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class AuthRemoteRepository {

    val authApi = Network.shared.ktorfitClient().createAuthApi()

    suspend fun getUserStatus(email: String, userId: String): ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokeApiCall { authApi.getUserStatus(email = email, userId = userId)}
    }

    suspend fun getUser(userId: String): ResultWrapper<BaseResponse<User>> {
        return Network.shared.invokeApiCall { authApi.getUser(userId = userId) }
    }

    suspend fun getUserEmailGroupsByEmail(email: String): ResultWrapper<BaseResponse<List<EmailGroupHeading>>> {
        return Network.shared.invokeApiCall { authApi.getUserEmailGroupsByEmail(email = email) }
    }

    suspend fun requestAuthToken(email: String, userId: String): ResultWrapper<BaseResponse<AuthToken>> {
        return Network.shared.invokeApiCall { authApi.requestAuthToken(email = email, userId = userId) }
    }

    suspend fun registerNewUser(user: User): ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokeApiCall { authApi.registerNewUser(user = user) }
    }

    suspend fun createNewToken(token: AuthToken): ResultWrapper<BaseResponse<AuthToken>> {
        return Network.shared.invokeApiCall { authApi.createNewToken(token = token) }
    }
}