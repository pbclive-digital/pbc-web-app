package com.kavi.pbc.web.news.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.data.news.News
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.news.data.model.NewsListUiState
import com.kavi.pbc.web.news.data.repository.local.NewsLocalRepository
import com.kavi.pbc.web.news.data.repository.remote.NewsRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsListViewModel: ViewModel() {

    val newsRemoteRepository = NewsRemoteRepository()
    val newsLocalRepository = NewsLocalRepository()

    private val _appAuthStatus = MutableStateFlow(AppAuthStatus.NONE)
    val appAuthStatus: StateFlow<AppAuthStatus> = _appAuthStatus
    private val _activeNewsList = MutableStateFlow<List<News>>(mutableListOf())
    val activeNewsList: StateFlow<List<News>> = _activeNewsList

    private val _activeNewsFetchStatus = MutableStateFlow(NewsListUiState.NONE)
    val activeNewsFetchStatus: StateFlow<NewsListUiState> = _activeNewsFetchStatus

    fun fetchAppAuthStatus() {
        _appAuthStatus.value = newsLocalRepository.getAppAuthStatus()
    }

    fun updateAuthStatus(authStatus: AppAuthStatus) {
        _appAuthStatus.value = authStatus
    }

    fun fetchActiveNewsList() {
        _activeNewsFetchStatus.value = NewsListUiState.PENDING
        viewModelScope.launch {
            when(val response = newsRemoteRepository.getActiveNews()) {
                is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError, is ResultWrapper.HttpError -> {
                    _activeNewsFetchStatus.value = NewsListUiState.FAILURE
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        if (it.isNotEmpty()) {
                            _activeNewsFetchStatus.value = NewsListUiState.SUCCESS
                            _activeNewsList.value = it
                        } else {
                            _activeNewsFetchStatus.value = NewsListUiState.EMPTY
                        }
                    }
                }
            }
        }
    }
}