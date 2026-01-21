package com.kavi.pbc.web.auth.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.auth.AuthToken
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class AuthRemoteRepository {

    suspend fun getUserStatus(email: String, userId: String): ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokeGET<String>(urlPath = "auth/get/$email/$userId")
    }

    suspend fun getUser(userId: String): ResultWrapper<BaseResponse<User>> {
        return Network.shared.invokeGET<User>(urlPath = "user/get/$userId")
    }

    suspend fun requestAuthToken(email: String, userId: String): ResultWrapper<BaseResponse<AuthToken>> {
        return Network.shared.invokeGET<AuthToken>(urlPath = "auth/get/token/$email/$userId")
    }

    suspend fun registerNewUser(user: User): ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokePOST<String, User>(urlPath = "user/create", params = user)
    }

    suspend fun createNewToken(token: AuthToken): ResultWrapper<BaseResponse<AuthToken>> {
        return Network.shared.invokePOST<AuthToken, AuthToken>(urlPath = "auth/create/token", params = token)
    }
}