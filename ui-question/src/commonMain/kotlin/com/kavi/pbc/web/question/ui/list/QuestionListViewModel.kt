package com.kavi.pbc.web.question.ui.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.pagination.PaginationRequest
import com.kavi.pbc.web.data.question.AnswerComment
import com.kavi.pbc.web.data.question.Question
import com.kavi.pbc.web.data.user.UserSummary
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.question.data.model.AddAnswerUiState
import com.kavi.pbc.web.question.data.model.DeleteQuestionUiState
import com.kavi.pbc.web.question.data.model.QuestionListUiState
import com.kavi.pbc.web.question.data.respository.remote.QuestionRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestionListViewModel: ViewModel() {

    val questionRemoteRepository = QuestionRemoteRepository()
    private val isInitialRequestFired = mutableStateOf(false)
    private val paginationRequest = PaginationRequest(null)
    private var isPagingReachedEnd by mutableStateOf(false)

    private val _pageIndex = MutableStateFlow(0)
    val pageIndex: StateFlow<Int> = _pageIndex
    private val _openQuestionList = MutableStateFlow<MutableList<Question>>(mutableListOf())
    val openQuestionList: StateFlow<MutableList<Question>> = _openQuestionList

    private val _openQuestionListUiState = MutableStateFlow(QuestionListUiState.NONE)
    val openQuestionListUiState: StateFlow<QuestionListUiState> = _openQuestionListUiState

    private val _personalQuestionList = MutableStateFlow<MutableList<Question>>(mutableListOf())
    val personalQuestionList: StateFlow<MutableList<Question>> = _personalQuestionList

    private val _personalQuestionListUiState = MutableStateFlow(QuestionListUiState.NONE)
    val personalQuestionListUiState: StateFlow<QuestionListUiState> = _personalQuestionListUiState

    private val _selectedQuestion = MutableStateFlow(Question())

    private val _answerCommentList = MutableStateFlow<MutableList<AnswerComment>>(mutableListOf())
    val answerCommentList: StateFlow<MutableList<AnswerComment>> = _answerCommentList

    private val _addAnswerStatus = MutableStateFlow(AddAnswerUiState.NONE)
    val addAnswerStatus: StateFlow<AddAnswerUiState> = _addAnswerStatus

    private val _questionDeleteUiState = MutableStateFlow(DeleteQuestionUiState.NONE)
    val questionDeleteUiState: StateFlow<DeleteQuestionUiState> = _questionDeleteUiState

    fun fetchOpenQuestionList(forceFetch: Boolean = false) {
        if (forceFetch) {
            paginationRequest.previousPageLastDocKey = null
            isInitialRequestFired.value = true
            getAllOpenQuestionList(true)
        } else {
            if (!isPagingReachedEnd) {
                if (!isInitialRequestFired.value && paginationRequest.previousPageLastDocKey == null) {
                    isInitialRequestFired.value = true
                    getAllOpenQuestionList()
                } else if (isInitialRequestFired.value && paginationRequest.previousPageLastDocKey != null) {
                    getAllOpenQuestionList()
                }
            }
        }
    }

    fun fetchPersonalQuestionList() {
        Session.user?.let { user ->
            viewModelScope.launch {
                when(val response = questionRemoteRepository.getPersonalQuestionList(userId = user.id!!)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _personalQuestionListUiState.value = QuestionListUiState.EMPTY
                    }
                    is ResultWrapper.Success -> {
                        _personalQuestionListUiState.value = QuestionListUiState.SUCCESS
                        response.value.body?.let {
                            _personalQuestionList.value = it
                        }
                    }
                }
            }
        }?: run {
            _personalQuestionListUiState.value = QuestionListUiState.EMPTY
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
                _addAnswerStatus.value = AddAnswerUiState.PENDING
                when(val response = questionRemoteRepository.createNewAnswer(_selectedQuestion.value.id!!, answerComment)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _addAnswerStatus.value = AddAnswerUiState.FAILURE
                    }
                    is ResultWrapper.Success -> {
                        _addAnswerStatus.value = AddAnswerUiState.SUCCESS
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

    fun revokeAddAnswerStatus() {
        _addAnswerStatus.value = AddAnswerUiState.NONE
    }

    fun deleteQuestion(questionId: String?) {
        questionId?.let { id ->
            _questionDeleteUiState.value = DeleteQuestionUiState.PENDING
            viewModelScope.launch {
                when(questionRemoteRepository.deleteQuestion(questionId = id)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _questionDeleteUiState.value = DeleteQuestionUiState.FAILURE
                    }
                    is ResultWrapper.Success -> {
                        _questionDeleteUiState.value = DeleteQuestionUiState.SUCCESS

                        // Remove from open questions list
                        _openQuestionList.value = _openQuestionList.value
                            .filterNot { it.id == id }
                            .toMutableList()

                        // Update the open question list Ui state if the list is empty after delete
                        if (_openQuestionList.value.isEmpty()) {
                            _openQuestionListUiState.value = QuestionListUiState.EMPTY
                        }

                        // Remove from personal questions list
                        _personalQuestionList.value = _personalQuestionList.value
                            .filterNot { it.id == id }
                            .toMutableList()

                        // Update the personal question list Ui state if the list is empty after delete
                        if (_openQuestionList.value.isEmpty()) {
                            _personalQuestionListUiState.value = QuestionListUiState.EMPTY
                        }
                    }
                }
            }
        }?: run {
            _questionDeleteUiState.value = DeleteQuestionUiState.NO_ID
        }
    }

    private fun getAllOpenQuestionList(replaceList: Boolean = false) {
        viewModelScope.launch {
            when(val response = questionRemoteRepository.getOpenQuestionList(paginationRequest = paginationRequest)) {
                is ResultWrapper.NetworkError -> {
                    _openQuestionListUiState.value = QuestionListUiState.FAILURE
                }
                is ResultWrapper.HttpError -> {
                    if (response.code == 404) {
                        isPagingReachedEnd = true
                    }
                    if (response.code == 416) {
                        _openQuestionListUiState.value = QuestionListUiState.EMPTY
                    }
                }
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _openQuestionListUiState.value = QuestionListUiState.SUCCESS
                        if (replaceList) {
                            _openQuestionList.value = it.entityList
                        } else {
                            _openQuestionList.update { currentList ->
                                (currentList + it.entityList).toMutableList()
                            }
                        }
                        paginationRequest.previousPageLastDocKey = it.previousPageLastDocKey
                    }
                }
            }
        }
    }
}