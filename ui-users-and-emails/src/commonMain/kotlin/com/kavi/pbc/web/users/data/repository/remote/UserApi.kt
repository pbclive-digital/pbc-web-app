package com.kavi.pbc.web.users.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.data.user.UserRoleUpdateReq
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path

interface UserApi {
    @GET("user/get/admins")
    suspend fun getAdminUsers(): BaseResponse<List<User>>

    @GET("user/get/resident-monks")
    suspend fun getResidentMonks(): BaseResponse<List<User>>

    @GET("user/get/consumers")
    suspend fun getConsumerUsers(): BaseResponse<List<User>>

    @PUT("user/update/userType")
    suspend fun modifyUserType(@Body userRoleUpdateReq: UserRoleUpdateReq): BaseResponse<User>

    @DELETE("user/delete/{userId}")
    suspend fun deleteConsumerUsers(@Path("userId") userId: String): BaseResponse<String>
}