package com.kavi.pbc.web.news.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kavi.pbc.web.data.news.News
import com.kavi.pbc.web.data.user.UserSummary
import com.kavi.pbc.web.network.model.ResultWrapper
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.news.data.model.NewsCreateOrModifyUiState
import com.kavi.pbc.web.news.data.repository.remote.NewsRemoteRepository
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock

class CreateOrModifyNewsViewModel: ViewModel() {

    val newsRemoteRepository = NewsRemoteRepository()

    private val _newsCreationOrModifyState = MutableStateFlow(NewsCreateOrModifyUiState.NONE)
    val newsCreationOrModifyState: StateFlow<NewsCreateOrModifyUiState> = _newsCreationOrModifyState

    // Make this news is nullable, because need to clear the news object when creation or modify complete
    private val _createOrModifyNews: MutableStateFlow<News?> = MutableStateFlow(News(
        createdTime = Clock.System.now().toEpochMilliseconds()
    ))
    val createOrModifyNews: StateFlow<News?> = _createOrModifyNews

    private var newsImageFile: PlatformFile? = null

    init {
        Session.user?.let {
            _createOrModifyNews.value = News(
                createdTime = Clock.System.now().toEpochMilliseconds(),
                author = UserSummary(
                    id = it.id!!, name = "${it.firstName} ${it.lastName}",
                    imageUrl = it.profilePicUrl
                )
            )
        }
    }

    fun setModifyNews(news: News) {
        _createOrModifyNews.value = news
    }

    fun updateNewsHeadline(headline: String) {
        _createOrModifyNews.value?.title = headline
    }

    fun updateNewsContent(content: String) {
        _createOrModifyNews.value?.content = content
    }

    fun updateNewsLink(newsLink: String) {
        _createOrModifyNews.value?.facebookLink = newsLink
    }

    fun updateNewsImageFile(newsImage: PlatformFile) {
        newsImageFile = newsImage
    }

    fun uploadNewsImageAndCreateOrModifyNews(isModify: Boolean = false) {
        if (isValidNewsForm()) {
            val formatedNewsTitle = _createOrModifyNews.value?.title
                ?.replace(" ", "_")
                ?.replace("-", "_")

            if (formatedNewsTitle != null && newsImageFile != null) {
                viewModelScope.launch {
                    when(val response = newsRemoteRepository.uploadNewsImage(formatedNewsTitle, newsImageFile!!)) {
                        is ResultWrapper.NetworkError, is ResultWrapper.UnAuthError -> {
                            // Do nothing for now
                        }
                        is ResultWrapper.HttpError -> {
                            if (isModify) {
                                updateNews()
                            } else {
                                createNews()
                            }
                        }
                        is ResultWrapper.Success -> {
                            response.value.body?.let {
                                _createOrModifyNews.value?.newsImage = it
                                if (isModify) {
                                    updateNews()
                                } else {
                                    createNews()
                                }
                            }
                        }
                    }
                }
            } else {
                if (isModify) {
                    updateNews()
                } else {
                    createNews()
                }
            }
        } else {
            _newsCreationOrModifyState.value = NewsCreateOrModifyUiState.EMPTY_FIELD
        }
    }

    fun clearNews() {
        _createOrModifyNews.value = null
    }

    fun revokeCreateOrModifyUiStatus() {
        _newsCreationOrModifyState.value = NewsCreateOrModifyUiState.NONE
    }

    private fun isValidNewsForm(): Boolean {
        return !(_createOrModifyNews.value?.title == null
                || _createOrModifyNews.value?.title?.isEmpty() == true
                || _createOrModifyNews.value?.content == null
                || _createOrModifyNews.value?.content?.isEmpty() == true
                )
    }

    private fun createNews() {
        viewModelScope.launch {
            when(val response = newsRemoteRepository.createNews(news = _createOrModifyNews.value!!)) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    _newsCreationOrModifyState.value = NewsCreateOrModifyUiState.FAILURE
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _newsCreationOrModifyState.value = NewsCreateOrModifyUiState.SUCCESS
                    }
                }
            }
        }
    }

    private fun updateNews() {
        viewModelScope.launch {
            when(val response = newsRemoteRepository.updateNews(newsId = _createOrModifyNews.value?.id!!, news = _createOrModifyNews.value!!)) {
                is ResultWrapper.NetworkError, is ResultWrapper.HttpError, is ResultWrapper.UnAuthError -> {
                    _newsCreationOrModifyState.value = NewsCreateOrModifyUiState.FAILURE
                }
                is ResultWrapper.Success -> {
                    response.value.body?.let {
                        _newsCreationOrModifyState.value = NewsCreateOrModifyUiState.SUCCESS
                    }
                }
            }
        }
    }
}