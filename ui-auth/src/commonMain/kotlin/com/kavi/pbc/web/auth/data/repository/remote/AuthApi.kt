package com.kavi.pbc.web.auth.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.auth.AuthToken
import com.kavi.pbc.web.data.user.User
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface AuthApi {

    @GET("auth/get/{email}/{userId}")
    suspend fun getUserStatus(@Path("email") email: String,
                              @Path("userId") userId: String): BaseResponse<String>

    @GET("user/get/{userId}")
    suspend fun getUser(@Path("userId") userId: String): BaseResponse<User>

    @GET("auth/get/token/{email}/{userId}")
    suspend fun requestAuthToken(@Path("email") email: String,
                                 @Path("userId") userId: String): BaseResponse<AuthToken>

    @POST("user/create")
    suspend fun registerNewUser(@Body user: User): BaseResponse<String>

    @POST("auth/create/token")
    suspend fun createNewToken(@Body token: AuthToken): BaseResponse<AuthToken>
}