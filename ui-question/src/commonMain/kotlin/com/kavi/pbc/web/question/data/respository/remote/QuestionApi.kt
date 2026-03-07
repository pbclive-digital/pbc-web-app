package com.kavi.pbc.web.question.data.respository.remote

import com.kavi.pbc.web.data.BaseResponse
import com.kavi.pbc.web.data.pagination.PaginationRequest
import com.kavi.pbc.web.data.pagination.PaginationResponse
import com.kavi.pbc.web.data.question.AnswerComment
import com.kavi.pbc.web.data.question.Question
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path

interface QuestionApi {

    @POST("question/get/all")
    suspend fun getOpenQuestionList(@Body paginationRequest: PaginationRequest?):
            BaseResponse<PaginationResponse<Question>>

    @GET("question/get/user/{userId}")
    suspend fun getPersonalQuestionList(@Path("userId") userId: String): BaseResponse<MutableList<Question>>

    @PUT("question/add/comment/{questionId}")
    suspend fun createNewAnswer(@Path("questionId") questionId: String,
                                @Body answerComment: AnswerComment): BaseResponse<Question>

    @POST("question/create")
    suspend fun createNewQuestion(@Body question: Question): BaseResponse<String>

    @PUT("question/update/{questionId}")
    suspend fun modifyQuestion(@Path("questionId") questionId: String,
                               @Body question: Question): BaseResponse<Question>
}