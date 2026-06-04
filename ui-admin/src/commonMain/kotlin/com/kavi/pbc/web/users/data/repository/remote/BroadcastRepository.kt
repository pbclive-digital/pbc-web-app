package com.kavi.pbc.web.users.data.repository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.email.record.EmailRecord
import com.kavi.pbc.web.data.pagination.PaginationRequest
import com.kavi.pbc.web.data.pagination.PaginationResponse
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class BroadcastRepository {

    val broadcastApi = Network.shared.ktorfitClient().createBroadcastApi()

    suspend fun getEmailRecordList(paginationRequest: PaginationRequest?):
            ResultWrapper<BaseResponse<PaginationResponse<EmailRecord>>> {
        return Network.shared.invokeApiCall {
            broadcastApi.getEmailRecords(paginationRequest = paginationRequest)
        }
    }
}