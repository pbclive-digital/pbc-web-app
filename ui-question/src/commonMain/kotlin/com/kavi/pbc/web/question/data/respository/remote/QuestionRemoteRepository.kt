package com.kavi.pbc.web.question.data.respository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.pagination.PaginationRequest
import com.kavi.pbc.web.data.pagination.PaginationResponse
import com.kavi.pbc.web.data.question.AnswerComment
import com.kavi.pbc.web.data.question.Question
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.network.model.ResultWrapper
import io.ktor.http.encodeURLPath

class QuestionRemoteRepository {

    suspend fun getOpenQuestionList(paginationRequest: PaginationRequest?):
            ResultWrapper<BaseResponse<PaginationResponse<Question>>> {
        return Network.shared
            .post<PaginationResponse<Question>, PaginationRequest>(urlPath = "question/get/all", body = paginationRequest)
    }

    suspend fun createNewAnswer(questionId: String, answerComment: AnswerComment):
            ResultWrapper<BaseResponse<Question>> {

        val encodedQuestionId = questionId.encodeURLPath()

        return Network.shared
            .put<Question, AnswerComment>(urlPath = "question/add/comment/${encodedQuestionId}", body = answerComment)
    }
}