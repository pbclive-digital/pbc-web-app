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

    val questionApi: QuestionApi = Network.shared.ktorfitClient().createQuestionApi()

    suspend fun getOpenQuestionList(paginationRequest: PaginationRequest?):
            ResultWrapper<BaseResponse<PaginationResponse<Question>>> {
        return Network.shared.invokeApiCall {
            questionApi.getOpenQuestionList(paginationRequest = paginationRequest)
        }
    }

    suspend fun getPersonalQuestionList(userId: String): ResultWrapper<BaseResponse<MutableList<Question>>> {
        return Network.shared.invokeApiCall {
            questionApi.getPersonalQuestionList(userId = userId)
        }
    }

    suspend fun createNewAnswer(questionId: String, answerComment: AnswerComment):
            ResultWrapper<BaseResponse<Question>> {
        return Network.shared.invokeApiCall {
            questionApi.createNewAnswer(questionId = questionId, answerComment = answerComment)
        }
    }

    suspend fun createNewQuestion(question: Question): ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokeApiCall {
            questionApi.createNewQuestion(question = question)
        }
    }

    suspend fun modifyQuestion(questionId: String, question: Question): ResultWrapper<BaseResponse<Question>> {
        return Network.shared.invokeApiCall {
            questionApi.modifyQuestion(questionId = questionId, question = question)
        }
    }

    suspend fun deleteQuestion(questionId: String): ResultWrapper<BaseResponse<String>> {
        return Network.shared.invokeApiCall { questionApi.deleteQuestion(questionId = questionId) }
    }
}