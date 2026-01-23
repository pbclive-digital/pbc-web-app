package com.kavi.pbc.web.question.ui.open

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.pagination.PaginationRequest
import com.kavi.pbc.web.data.question.Question
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.question.data.model.OpenQuestionListUiState
import com.kavi.pbc.web.question.data.respository.remote.QuestionRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OpenQuestionListViewModel: ViewModel() {

    val questionRemoteRepository = QuestionRemoteRepository()
    private val isInitialRequestFired = mutableStateOf(false)
    private val paginationRequest = PaginationRequest(null)
    private var isPagingReachedEnd by mutableStateOf(false)

    private val _pageIndex = MutableStateFlow(0)
    val pageIndex: StateFlow<Int> = _pageIndex
    private val _openQuestionList = MutableStateFlow<MutableList<Question>>(mutableListOf())
    val openQuestionList: StateFlow<MutableList<Question>> = _openQuestionList
    private val _openQuestionListUiState = MutableStateFlow(OpenQuestionListUiState.NONE)
    val openQuestionListUiState: StateFlow<OpenQuestionListUiState> = _openQuestionListUiState

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