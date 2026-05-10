package com.kavi.pbc.web.question.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.question.PrivacyStatus
import com.kavi.pbc.web.data.question.Question
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.question.data.model.NewQuestionUiState
import com.kavi.pbc.web.question.data.respository.remote.QuestionRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuestionAskOrModifyViewModel: ViewModel() {
    val questionRemoteRepository = QuestionRemoteRepository()

    // Make this question is nullable, because need to clear the question object when creation or modify complete
    private val _askOrModifyQuestion: MutableStateFlow<Question?> = MutableStateFlow(Question())
    val askOrModifyQuestion: StateFlow<Question?> = _askOrModifyQuestion

    private val _questionAskOrModifyStatus = MutableStateFlow(NewQuestionUiState.NONE)
    val questionAskOrModifyStatus: StateFlow<NewQuestionUiState> = _questionAskOrModifyStatus

    init {
        Session.user?.let {
            _askOrModifyQuestion.value = Question(
                authorId = it.id!!,
                author = it
            )
        }
    }

    fun setModifyingQuestion(question: Question) {
        _askOrModifyQuestion.value = question
    }

    fun initiateNewQuestion() {
        Session.user?.let {
            _askOrModifyQuestion.value = Question(
                authorId = it.id!!,
                author = it
            )
        }
    }

    fun clearQuestion() {
        _askOrModifyQuestion.value = null
    }

    fun updateQuestionTitle(title: String) {
        _askOrModifyQuestion.value?.title = title
    }

    fun updateQuestionContent(content: String) {
        _askOrModifyQuestion.value?.content = content
    }

    fun updatePrivacyStatus(isPrivate: Boolean) {
        if (isPrivate)
            _askOrModifyQuestion.value?.privacy = PrivacyStatus.PRIVATE
        else
            _askOrModifyQuestion.value?.privacy = PrivacyStatus.PUBLIC
    }

    fun createOrModifyQuestion(isModify: Boolean) {
        if (!isModify) {
            viewModelScope.launch {
                _questionAskOrModifyStatus.value = NewQuestionUiState.PENDING
                when(questionRemoteRepository.createNewQuestion(_askOrModifyQuestion.value!!)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _questionAskOrModifyStatus.value = NewQuestionUiState.FAILURE
                    }
                    is ResultWrapper.Success -> {
                        _questionAskOrModifyStatus.value = NewQuestionUiState.SUCCESS
                    }
                }
            }
        } else {
            viewModelScope.launch {
                _questionAskOrModifyStatus.value = NewQuestionUiState.PENDING
                when(questionRemoteRepository.modifyQuestion(_askOrModifyQuestion.value!!.id!!, _askOrModifyQuestion.value!!)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                        _questionAskOrModifyStatus.value = NewQuestionUiState.FAILURE
                    }
                    is ResultWrapper.Success -> {
                        _questionAskOrModifyStatus.value = NewQuestionUiState.SUCCESS
                    }
                }
            }
        }
    }

    fun revokeNewQuestionUiState() {
        _questionAskOrModifyStatus.value = NewQuestionUiState.NONE
    }
}