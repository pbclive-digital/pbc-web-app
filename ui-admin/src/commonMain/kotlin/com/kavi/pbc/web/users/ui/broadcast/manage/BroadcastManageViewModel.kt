package com.kavi.pbc.web.users.ui.broadcast.manage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.email.record.EmailRecord
import com.kavi.pbc.web.data.pagination.PaginationRequest
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.users.data.model.EmailRecordUiState
import com.kavi.pbc.web.users.data.repository.remote.BroadcastRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BroadcastManageViewModel: ViewModel() {

    val broadcastRepository = BroadcastRepository()

    private val paginationRequest = PaginationRequest(null)
    private var isPagingReachedEnd by mutableStateOf(false)

    private val _emailRecordList = MutableStateFlow<MutableList<EmailRecord>>(mutableListOf())
    val emailRecordList: StateFlow<MutableList<EmailRecord>> = _emailRecordList

    private val _emailRecordUiState = MutableStateFlow(EmailRecordUiState.NONE)
    val emailRecordUiState: StateFlow<EmailRecordUiState> = _emailRecordUiState

    fun fetchEmailRecordList(replaceList: Boolean = false) {
        viewModelScope.launch {
            _emailRecordUiState.value = EmailRecordUiState.PENDING
            when(val response = broadcastRepository.getEmailRecordList(paginationRequest = paginationRequest)) {
                is ResultWrapper.NetworkError -> {
                    _emailRecordUiState.value = EmailRecordUiState.FAILURE
                }
                is ResultWrapper.HttpError -> {
                    if (response.code == 404) {
                        isPagingReachedEnd = true
                    }
                    if (response.code == 416) {
                        _emailRecordUiState.value = EmailRecordUiState.EMPTY
                    }
                }
                is ResultWrapper.UnAuthError -> {}
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _emailRecordUiState.value = EmailRecordUiState.SUCCESS
                        if (replaceList) {
                            _emailRecordList.value = it.entityList
                        } else {
                            _emailRecordList.update { currentList ->
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