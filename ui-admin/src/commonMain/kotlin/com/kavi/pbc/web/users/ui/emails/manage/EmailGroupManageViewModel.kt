package com.kavi.pbc.web.users.ui.emails.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.email.EmailGroup
import com.kavi.pbc.web.data.email.EmailGroupHeading
import com.kavi.pbc.web.data.email.EmailItem
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.users.data.repository.remote.EmailGroupRepository
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmailGroupManageViewModel: ViewModel() {

    val emailGroupRemoteRepository = EmailGroupRepository()

    private val _emailGroupHeadings = MutableStateFlow<List<EmailGroupHeading>>(mutableListOf())
    val emailGroupHeadings: StateFlow<List<EmailGroupHeading>> = _emailGroupHeadings

    private val _selectedEmailGroup = MutableStateFlow(EmailGroup())

    private val _letterGroupedEmailList = MutableStateFlow(mapOf<Char, List<EmailItem>>())
    val letterGroupedEmailList: StateFlow<Map<Char, List<EmailItem>>> = _letterGroupedEmailList

    private val _selectedGroupEmailCount = MutableStateFlow(0)
    val selectedGroupEmailCount: StateFlow<Int> = _selectedGroupEmailCount

    fun createEmailGroupWithCSVFile(groupName: String, uploadedCsvFile: PlatformFile) {
        viewModelScope.launch {
            when(val response = emailGroupRemoteRepository.createEmailGroupFromFile(
                groupName, uploadedCsvFile
            )) {
                is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                    // Do nothing for now
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        fetchEmailGroupHeadings()
                    }
                }
            }
        }
    }

    fun fetchEmailGroupHeadings() {
        viewModelScope.launch {
            when(val response = emailGroupRemoteRepository.getEmailGroupHeadings()) {
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

    fun fetchEmailGroupEmailList(groupId: String) {
        if (groupId.isNotEmpty()) {
            viewModelScope.launch {
                when (val response = emailGroupRemoteRepository.getEmailGroup(groupId = groupId)) {
                    is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                        // Do nothing for now
                    }
                    is ResultWrapper.Success -> {
                        response.value.body?.let {
                            _selectedEmailGroup.value = it
                            _selectedGroupEmailCount.value = it.emails.size
                            categorizeEmailList()
                        }
                    }
                }
            }
        }
    }

    fun addEmailToEmailGroup(selectedGroupId: String, emailItem: EmailItem) {
        val emailItemList = listOf(emailItem)
        viewModelScope.launch {
            when (val response = emailGroupRemoteRepository
                .addEmailToEmailGroup(groupId = selectedGroupId, emailItemList = emailItemList)) {
                is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                    // Do nothing for now
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _selectedEmailGroup.value = it
                        categorizeEmailList()
                    }
                }
            }
        }
    }

    fun removeEmailFromEmailGroup(selectedGroupId: String, emailItem: EmailItem) {
        val emailItemList = listOf(emailItem)
        viewModelScope.launch {
            when (val response = emailGroupRemoteRepository
                .removeEmailFromEmailGroup(groupId = selectedGroupId, emailItemList = emailItemList)) {
                is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                    // Do nothing for now
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _selectedEmailGroup.value = it
                        categorizeEmailList()
                    }
                }
            }
        }
    }

    fun deleteEmailGroup(deletingGroupId: String) {
        viewModelScope.launch {
            when (val response = emailGroupRemoteRepository.deleteEmailGroup(groupId = deletingGroupId)) {
                is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                    // Do nothing for now
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _emailGroupHeadings.value = _emailGroupHeadings.value
                            .filterNot { it.id == deletingGroupId }
                            .toMutableList()
                    }
                }
            }
        }
    }

    private fun categorizeEmailList() {
        _letterGroupedEmailList.value = _selectedEmailGroup.value.emails
            .filter { it.email.isNotEmpty() } // Filter items only email available
            .groupBy { it.email.first().lowercaseChar() } // Grouped by first letter of the email
            .toList() // Convert to list
            .sortedBy { (key, _) -> key } // Sort by map key
            .toMap() // Convert back to map
    }
}