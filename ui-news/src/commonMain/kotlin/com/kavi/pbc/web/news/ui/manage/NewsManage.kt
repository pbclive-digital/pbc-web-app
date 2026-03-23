package com.kavi.pbc.web.news.ui.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kavi.pbc.web.common.ui.component.AppButtonWithIcon
import com.kavi.pbc.web.common.ui.component.TitleWithActionComposable
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.news.News
import com.kavi.pbc.web.news.data.model.NewsManageMode
import com.kavi.pbc.web.news.ui.common.NewsItemForAdmin
import com.kavi.pbc.web.news.ui.common.SelectedNewsUI
import com.kavi.pbc.web.news.ui.create.CreateOrModifyNewsDialog
import com.kavi.pbc.web.news.ui.manage.dialog.DeleteConfirmationDialog
import com.kavi.pbc.web.news.ui.manage.dialog.PublishConfirmationDialog
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_news.generated.resources.Res
import pbcwebapp.ui_news.generated.resources.news_icon_news
import pbcwebapp.ui_news.generated.resources.news_label_active
import pbcwebapp.ui_news.generated.resources.news_label_create_news
import pbcwebapp.ui_news.generated.resources.news_label_draft
import pbcwebapp.ui_news.generated.resources.news_label_manage
import pbcwebapp.ui_news.generated.resources.news_label_no_active
import pbcwebapp.ui_news.generated.resources.news_label_no_draft
import pbcwebapp.ui_news.generated.resources.news_phrase_manage

@Composable
fun NewsManageUI(navController: NavController) {
    val viewModel: NewsManageViewModel = viewModel { NewsManageViewModel() }
    val selectedNews = remember { mutableStateOf(News()) }
    val isInitialNewsSelected = remember { mutableStateOf(false) }

    val newsMode = remember { mutableStateOf(NewsManageMode.UNSELECTED) }
    val showPublishConfirmationDialog = remember { mutableStateOf(false) }
    val publishingNewsId = remember { mutableStateOf("") }
    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }
    val deletingNewsId = remember { mutableStateOf("") }

    val selectedNewsForModify: MutableState<News?> = remember { mutableStateOf(null) }
    val showCreateOrModifyDialog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 12.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TitleWithActionComposable(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                titleText = stringResource(Res.string.news_label_manage)
            ) {
                AppButtonWithIcon(
                    modifier = Modifier.padding(bottom = 12.dp),
                    label = stringResource(Res.string.news_label_create_news),
                    icon = painterResource(Res.drawable.news_icon_news),
                    cornerRadius = 12.dp
                ) {
                    showCreateOrModifyDialog.value = true
                }
            }

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 30.dp)
            ) {
                Text(
                    text = stringResource(Res.string.news_phrase_manage),
                    fontFamily = PBCFontFamily,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row {
                    Column (
                        modifier = Modifier
                            .weight(.35f)
                            .verticalScroll(state = rememberScrollState())
                    ) {
                        DraftedNews(
                            viewModel = viewModel,
                            isSelectedUpdated = isInitialNewsSelected,
                            publishConfirmation = showPublishConfirmationDialog,
                            publishingId = publishingNewsId,
                            deleteConfirmation = showDeleteConfirmationDialog,
                            deletingId = deletingNewsId,
                            showModify = showCreateOrModifyDialog,
                            selectedForModify = selectedNewsForModify,
                            newsMode = newsMode,
                        ) { news ->
                            selectedNews.value = news
                        }
                        ActiveNews(
                            viewModel = viewModel,
                            isSelectedUpdated = isInitialNewsSelected,
                            deleteConfirmation = showDeleteConfirmationDialog,
                            deletingId = deletingNewsId,
                            showModify = showCreateOrModifyDialog,
                            selectedForModify = selectedNewsForModify,
                            newsMode = newsMode
                        ) { news ->
                            selectedNews.value = news
                        }
                    }

                    Column (modifier = Modifier.weight(.65f)) {
                        SelectedNewsUI(
                            modifier = Modifier.padding(start = 8.dp),
                            selectedNews = selectedNews
                        )
                    }
                }
            }
        }
    }

    DeleteConfirmationDialog(
        showDialog = showDeleteConfirmationDialog,
        onAgree = {
            showDeleteConfirmationDialog.value = false
            viewModel.deleteNews(newsId = deletingNewsId.value, newsMode = newsMode.value)
            deletingNewsId.value = ""
            newsMode.value = NewsManageMode.UNSELECTED
        },
        onDisagree = {
            showDeleteConfirmationDialog.value = false
            deletingNewsId.value = ""
            newsMode.value = NewsManageMode.UNSELECTED
        }
    )

    PublishConfirmationDialog(
        showDialog = showPublishConfirmationDialog,
        onAgree = {
            showPublishConfirmationDialog.value = false
            viewModel.publishDraftNews(newsId = publishingNewsId.value)
            publishingNewsId.value = ""
        },
        onDisagree = {
            showPublishConfirmationDialog.value = false
            publishingNewsId.value = ""
        }
    )

    CreateOrModifyNewsDialog(
        showDialog = showCreateOrModifyDialog,
        modifyNews = selectedNewsForModify.value,
        onCreateOrModify = {
            showCreateOrModifyDialog.value = false
            selectedNewsForModify.value = null

            // Update the draft news list
            viewModel.fetchDraftNewsList()
            // Update the active news list
            viewModel.fetchActiveNewsList()
        },
        onDismiss = {
            showCreateOrModifyDialog.value = false
            selectedNewsForModify.value = null
        }
    )
}

@Composable
fun DraftedNews(
    viewModel: NewsManageViewModel,
    isSelectedUpdated: MutableState<Boolean>,
    publishConfirmation: MutableState<Boolean>,
    publishingId: MutableState<String>,
    deleteConfirmation: MutableState<Boolean>,
    deletingId: MutableState<String>,
    showModify: MutableState<Boolean>,
    selectedForModify: MutableState<News?>,
    newsMode: MutableState<NewsManageMode>,
    onSelect:(news: News) -> Unit
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    val draftNewsList by viewModel.draftNewsList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDraftNewsList()
    }

    Text(
        text = stringResource(Res.string.news_label_draft),
        fontFamily = PBCFontFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
    )

    Column {
        if (draftNewsList.isNotEmpty()) {
            // Set initial selected NEWS
            onSelect.invoke(draftNewsList[0])
            isSelectedUpdated.value = true

            draftNewsList.forEachIndexed { index, news ->
                NewsItemForAdmin(modifier = Modifier.clickable {
                    onSelect.invoke(news)
                }, news = news, isDraftNews = true, onModify = {
                    selectedForModify.value = news
                    showModify.value = true
                }, onPublish = {
                    publishConfirmation.value = true
                    publishingId.value = news.id!!
                }, onDelete = {
                    deleteConfirmation.value = true
                    newsMode.value = NewsManageMode.DRAFT
                    deletingId.value = news.id!!
                })
                if (index < draftNewsList.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = themeAdditionalColors.shadow
                    )
                }
            }
        } else {
            Box (
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.news_label_no_draft),
                    textAlign = TextAlign.Center,
                    fontFamily = PBCFontFamily,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
fun ActiveNews(
    viewModel: NewsManageViewModel,
    isSelectedUpdated: MutableState<Boolean>,
    deleteConfirmation: MutableState<Boolean>,
    deletingId: MutableState<String>,
    showModify: MutableState<Boolean>,
    selectedForModify: MutableState<News?>,
    newsMode: MutableState<NewsManageMode>,
    onSelect:(news: News) -> Unit
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    val activeNewsList by viewModel.activeNewsList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchActiveNewsList()
    }

    Text(
        text = stringResource(Res.string.news_label_active),
        fontFamily = PBCFontFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
    )

    Column {
        if (activeNewsList.isNotEmpty()) {
            if (!isSelectedUpdated.value) {
                // Set initial selected NEWS
                onSelect.invoke(activeNewsList[0])
                isSelectedUpdated.value = true
            }

            activeNewsList.forEachIndexed { index, news ->
                NewsItemForAdmin(modifier = Modifier.clickable {
                    onSelect.invoke(news)
                }, news = news, isDraftNews = false, onModify = {
                    selectedForModify.value = news
                    showModify.value = true
                }, onPublish = {
                    /* Nothing to implement */
                }, onDelete = {
                    deleteConfirmation.value = true
                    newsMode.value = NewsManageMode.ACTIVE
                    deletingId.value = news.id!!
                })
                if (index < activeNewsList.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = themeAdditionalColors.shadow
                    )
                }
            }
        } else {
            Box (
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.news_label_no_active),
                    textAlign = TextAlign.Center,
                    fontFamily = PBCFontFamily,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}