package com.kavi.pbc.web.users.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.email.EmailGroup
import com.kavi.pbc.web.data.email.EmailGroupHeading
import com.kavi.pbc.web.data.email.EmailItem
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.request.forms.MultiPartFormDataContent

interface EmailGroupApi {

    @POST("email-group/create")
    suspend fun createEmailGroup(@Body emailGroup: EmailGroup): BaseResponse<String>

    @Multipart
    @POST("email-group/create/from-file/{groupName}")
    suspend fun createEmailGroupFromFile(@Path("groupName") groupName: String,
                                         @Body file: MultiPartFormDataContent): BaseResponse<String>

    @GET("email-group/get/all")
    suspend fun getEmailGroupHeadings(): BaseResponse<List<EmailGroupHeading>>

    @GET("email-group/get/{groupId}")
    suspend fun getEmailGroup(@Path("groupId") groupId: String): BaseResponse<EmailGroup>

    @PUT("email-group/add/emails/{groupId}")
    suspend fun addEmailToEmailGroup(@Path("groupId") groupId: String,
                                     @Body emailItemList: List<EmailItem>): BaseResponse<EmailGroup>

    @PUT("email-group/remove/emails/{groupId}")
    suspend fun removeEmailFromEmailGroup(@Path("groupId") groupId: String,
                                     @Body emailItemList: List<EmailItem>): BaseResponse<EmailGroup>

    @DELETE("email-group/delete/{groupId}")
    suspend fun deleteEmailGroup(@Path("groupId") groupId: String): BaseResponse<String>
}