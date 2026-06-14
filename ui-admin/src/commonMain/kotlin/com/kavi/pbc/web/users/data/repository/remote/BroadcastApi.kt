package com.kavi.pbc.web.users.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.email.EmailGroupHeading
import com.kavi.pbc.web.data.email.record.EmailRecord
import com.kavi.pbc.web.data.email.send.EmailBroadcastRequest
import com.kavi.pbc.web.data.pagination.PaginationRequest
import com.kavi.pbc.web.data.pagination.PaginationResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST

interface BroadcastApi {

    @POST("broadcast/get/records")
    suspend fun getEmailRecords(@Body paginationRequest: PaginationRequest?):
            BaseResponse<PaginationResponse<EmailRecord>>

    @GET("email-group/get/all")
    suspend fun getEmailGroupHeadings(): BaseResponse<List<EmailGroupHeading>>

    @POST("broadcast/v2/email")
    suspend fun sendBroadcastEmail(@Body emailBroadcastReq: EmailBroadcastRequest):
            BaseResponse<String>
}