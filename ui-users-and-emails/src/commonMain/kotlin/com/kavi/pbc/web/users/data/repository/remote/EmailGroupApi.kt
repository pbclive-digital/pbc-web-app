package com.kavi.pbc.web.users.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.email.EmailGroup
import com.kavi.pbc.web.data.email.EmailGroupHeading
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface EmailGroupApi {

    @POST("email-group/create")
    suspend fun createEmailGroup(@Body emailGroup: EmailGroup): BaseResponse<String>

    @GET("email-group/get/all")
    suspend fun getEmailGroupHeadings(): BaseResponse<List<EmailGroupHeading>>

    @GET("email-group/get/{groupId}")
    suspend fun getEmailGroup(@Path("groupId") groupId: String): BaseResponse<EmailGroup>
}