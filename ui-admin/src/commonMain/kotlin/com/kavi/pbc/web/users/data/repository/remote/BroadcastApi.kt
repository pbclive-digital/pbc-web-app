package com.kavi.pbc.web.users.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.email.record.EmailRecord
import com.kavi.pbc.web.data.pagination.PaginationRequest
import com.kavi.pbc.web.data.pagination.PaginationResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST

interface BroadcastApi {

    @POST("question/get/all")
    suspend fun getEmailRecords(@Body paginationRequest: PaginationRequest?):
            BaseResponse<PaginationResponse<EmailRecord>>
}