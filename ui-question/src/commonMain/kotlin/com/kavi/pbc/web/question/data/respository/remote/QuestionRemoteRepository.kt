package com.kavi.pbc.web.question.data.respository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.pagination.PaginationRequest
import com.kavi.pbc.web.data.pagination.PaginationResponse
import com.kavi.pbc.web.data.question.Question
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper

class QuestionRemoteRepository {

    suspend fun getOpenQuestionList(paginationRequest: PaginationRequest?):
            ResultWrapper<BaseResponse<PaginationResponse<Question>>> {
        return Network.shared
            .post<PaginationResponse<Question>, PaginationRequest>(urlPath = "question/get/all", body = paginationRequest)
    }
}