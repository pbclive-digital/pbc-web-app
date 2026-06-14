package com.kavi.pbc.web.users.ui.broadcast.send

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.email.EmailGroupHeading
import com.kavi.pbc.web.data.email.send.EmailBroadcastMsg
import com.kavi.pbc.web.data.email.send.EmailBroadcastRequest
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.users.data.model.SendBroadcastUiState
import com.kavi.pbc.web.users.data.repository.remote.BroadcastRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SendBroadcastViewModel: ViewModel() {

    val broadcastRepository = BroadcastRepository()

    private val _sendBroadcastState = MutableStateFlow(SendBroadcastUiState.NONE)
    val sendBroadcastState: StateFlow<SendBroadcastUiState> = _sendBroadcastState

    private val _emailGroupHeadings = MutableStateFlow<List<EmailGroupHeading>>(mutableListOf())
    val emailGroupHeadings: StateFlow<List<EmailGroupHeading>> = _emailGroupHeadings

    private val _emailBroadcastMsg: MutableStateFlow<EmailBroadcastMsg> = MutableStateFlow(EmailBroadcastMsg())

    private val _emailBroadcastReq: MutableStateFlow<EmailBroadcastRequest> = MutableStateFlow(
        EmailBroadcastRequest(
            emailBroadcastMessage = _emailBroadcastMsg.value,
            emailGroupHeadings = emptyList()
        )
    )

    fun updateEmailSubject(subject: String) {
        _emailBroadcastMsg.value.subject = subject
    }

    fun updateEmailTitle(title: String) {
        _emailBroadcastMsg.value.title = title
    }

    fun updateEmailMessage(message: String) {
        _emailBroadcastMsg.value.message = message
    }

    fun updateSelectedEmailGroups(emailGroupHeadings: List<EmailGroupHeading>) {
        _emailBroadcastReq.value.emailGroupHeadings = emailGroupHeadings
    }

    fun revokeSendBroadcastUiState() {
        _sendBroadcastState.value = SendBroadcastUiState.NONE
    }

    fun fetchEmailGroupHeadings() {
        if (_emailGroupHeadings.value.isEmpty()) {
            viewModelScope.launch {
                when (val response = broadcastRepository.getEmailGroupHeadings()) {
                    is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                        // Do nothing for now
                    }

                    is ResultWrapper.Success -> {
                        response.value.body?.let {
                            _emailGroupHeadings.value = it
                        }
                    }
                }
            }
        }
    }

    fun sendBroadcastEmail() {
        viewModelScope.launch {
            if (validateForm()) {
                _sendBroadcastState.value = SendBroadcastUiState.PENDING
                when (val response =
                    broadcastRepository.sendBroadcastEmail(_emailBroadcastReq.value)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                        _sendBroadcastState.value = SendBroadcastUiState.FAILURE
                    }

                    is ResultWrapper.Success -> {
                        response.value.body?.let {
                            _sendBroadcastState.value = SendBroadcastUiState.SUCCESS
                        }
                    }
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        if (_emailBroadcastReq.value.emailGroupHeadings.isEmpty()) {
            _sendBroadcastState.value = SendBroadcastUiState.EMPTY_EMAIL_GROUP
            return false
        }

        if (_emailBroadcastMsg.value.subject.isBlank() || _emailBroadcastMsg.value.title.isBlank() || _emailBroadcastMsg.value.message.isBlank()) {
            _sendBroadcastState.value = SendBroadcastUiState.FORM_INVALID
            return false
        }

        return true
    }
}