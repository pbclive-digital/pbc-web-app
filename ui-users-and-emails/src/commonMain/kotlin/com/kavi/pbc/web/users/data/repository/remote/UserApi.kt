package com.kavi.pbc.web.users.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.user.User
import de.jensklingenberg.ktorfit.http.GET

interface UserApi {
    @GET("user/get/admins")
    suspend fun getAdminUsers(): BaseResponse<List<User>>

    @GET("user/get/consumers")
    suspend fun getConsumerUsers(): BaseResponse<List<User>>
}