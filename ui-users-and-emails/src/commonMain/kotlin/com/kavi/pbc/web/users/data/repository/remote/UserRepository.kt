package com.kavi.pbc.web.users.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class UserRepository {
    val userApi = Network.shared.ktorfitClient().createUserApi()

    suspend fun getAdminUserList(): ResultWrapper<BaseResponse<List<User>>> {
        return Network.shared.invokeApiCall { userApi.getAdminUsers() }
    }

    suspend fun getConsumerUserList(): ResultWrapper<BaseResponse<List<User>>> {
        return Network.shared.invokeApiCall { userApi.getConsumerUsers() }
    }
}