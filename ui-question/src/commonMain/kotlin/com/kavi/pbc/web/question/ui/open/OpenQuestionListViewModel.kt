package com.kavi.pbc.web.question.ui.open

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.data.pagination.PaginationRequest
import com.kavi.pbc.web.data.question.AnswerComment
import com.kavi.pbc.web.data.question.Question
import com.kavi.pbc.web.data.user.UserSummary
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.question.data.model.AddAnswerStatus
import com.kavi.pbc.web.question.data.model.OpenQuestionListUiState
import com.kavi.pbc.web.question.data.respository.local.QuestionLocalRepository
import com.kavi.pbc.web.question.data.respository.remote.QuestionRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

class OpenQuestionListViewModel: ViewModel() {

    val questionRemoteRepository = QuestionRemoteRepository()
    val questionLocalRepository = QuestionLocalRepository()
    private val isInitialRequestFired = mutableStateOf(false)
    private val paginationRequest = PaginationRequest(null)
    private var isPagingReachedEnd by mutableStateOf(false)

    private val _appAuthStatus = MutableStateFlow(AppAuthStatus.NONE)
    val appAuthStatus: StateFlow<AppAuthStatus> = _appAuthStatus
    private val _pageIndex = MutableStateFlow(0)
    val pageIndex: StateFlow<Int> = _pageIndex
    private val _openQuestionList = MutableStateFlow<MutableList<Question>>(mutableListOf())
    val openQuestionList: StateFlow<MutableList<Question>> = _openQuestionList
    private val _openQuestionListUiState = MutableStateFlow(OpenQuestionListUiState.NONE)
    val openQuestionListUiState: StateFlow<OpenQuestionListUiState> = _openQuestionListUiState

    private val _selectedQuestion = MutableStateFlow(Question())
    val selectedQuestion: StateFlow<Question> = _selectedQuestion

    private val _answerCommentList = MutableStateFlow<MutableList<AnswerComment>>(mutableListOf())
    val answerCommentList: StateFlow<MutableList<AnswerComment>> = _answerCommentList

    private val _addAnswerStatus = MutableStateFlow(AddAnswerStatus.NONE)
    val addAnswerStatus: StateFlow<AddAnswerStatus> = _addAnswerStatus

    fun fetchAppAuthStatus() {
        _appAuthStatus.value = questionLocalRepository.getAppAuthStatus()
    }

    fun updateAuthStatus(authStatus: AppAuthStatus) {
        _appAuthStatus.value = authStatus
    }

    fun fetchOpenQuestionList() {
        if (!isPagingReachedEnd) {
            if (!isInitialRequestFired.value && paginationRequest.previousPageLastDocKey == null) {
                isInitialRequestFired.value = true
                getAllOpenQuestionList()
            } else if (isInitialRequestFired.value && paginationRequest.previousPageLastDocKey != null) {
                getAllOpenQuestionList()
            }
        }
    }

    fun setSelectedQuestion(question: Question) {
        _selectedQuestion.value = question
        _answerCommentList.value = question.answerList
    }

    fun addAnswerCommentToQuestion(givenAnswerComment: String) {
        Session.user?.let { user ->
            val answerComment = AnswerComment(
                comment = givenAnswerComment,
                author = UserSummary(
                    id = user.id!!,
                    name = "${user.firstName} ${user.lastName}",
                    imageUrl = user.profilePicUrl
                )
            )

            viewModelScope.launch {
                _addAnswerStatus.value = AddAnswerStatus.PENDING
                when(val response = questionRemoteRepository.createNewAnswer(_selectedQuestion.value.id!!, answerComment)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _addAnswerStatus.value = AddAnswerStatus.FAILURE
                    }
                    is ResultWrapper.Success -> {
                        _addAnswerStatus.value = AddAnswerStatus.SUCCESS
                        response.value.body?.let {
                            _answerCommentList.update { currentList ->
                                ((currentList + answerComment) as MutableList<AnswerComment>)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getAllOpenQuestionList() {
        viewModelScope.launch {
            when(val response = questionRemoteRepository.getOpenQuestionList(paginationRequest = paginationRequest)) {
                is ResultWrapper.NetworkError -> {
                    _openQuestionListUiState.value = OpenQuestionListUiState.FAILURE
                }
                is ResultWrapper.HttpError -> {
                    if (response.code == 404) {
                        isPagingReachedEnd = true
                    }
                }
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _openQuestionListUiState.value = OpenQuestionListUiState.SUCCESS
                        _openQuestionList.update { currentList ->
                            (currentList + it.entityList).toMutableList()
                        }
                        paginationRequest.previousPageLastDocKey = it.previousPageLastDocKey
                    }
                }
            }
        }
    }
}