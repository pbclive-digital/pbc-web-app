package com.kavi.pbc.web.news.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kavi.pbc.web.common.ui.component.AppFullScreenLoader
import com.kavi.pbc.web.data.news.News
import com.kavi.pbc.web.news.data.model.NewsListUiState
import com.kavi.pbc.web.news.ui.common.NewsItem
import com.kavi.pbc.web.news.ui.sheet.NewsSelectedBottomSheetUI
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.news.ui.common.SelectedNewsUI
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_news.generated.resources.Res
import pbcwebapp.ui_news.generated.resources.news_label_active_fetch_empty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListUI(navController: NavController) {

    val viewModel: NewsListViewModel = viewModel { NewsListViewModel() }

    val activeNewsFetchStatus by viewModel.activeNewsFetchStatus.collectAsState()
    val activeNewsList by viewModel.activeNewsList.collectAsState()

    val selectedNews = remember { mutableStateOf(News()) }

    val selectedNewsSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val showNewsSheet = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchActiveNewsList()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxHeight = this.maxHeight
        val maxWidth = this.maxWidth

        when(activeNewsFetchStatus) {
            NewsListUiState.NONE -> {}
            NewsListUiState.EMPTY -> {
                EmptyNewsList()
            }
            NewsListUiState.PENDING -> {
                AppFullScreenLoader(isWithBackground = false)
            }
            NewsListUiState.FAILURE -> {}
            NewsListUiState.SUCCESS -> {
                Row {
                    when (UIUtil.screenType(maxWidth)) {
                        ScreenType.PHONE -> {
                            Column(
                                modifier = Modifier
                                    .height(maxHeight)
                                    .padding(top = 10.dp)
                            ) {
                                LazyColumn {
                                    itemsIndexed(activeNewsList) { index, news ->
                                        NewsItem(
                                            news = news, onReadMore = {
                                                selectedNews.value = news
                                                showNewsSheet.value = true
                                            }
                                        )
                                        if (index < activeNewsList.lastIndex) {
                                            HorizontalDivider(
                                                modifier = Modifier.fillMaxWidth(),
                                                thickness = 1.dp,
                                                color = Color.LightGray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        ScreenType.TABLET, ScreenType.COMPUTER -> {
                            selectedNews.value = activeNewsList[0]
                            Column(
                                modifier = Modifier
                                    .weight(.35f)
                                    .height(maxHeight)
                                    .padding(top = 10.dp, end = 15.dp)
                            ) {
                                LazyColumn {
                                    itemsIndexed(activeNewsList) { index, news ->
                                        NewsItem(
                                            news = news, onReadMore = {
                                                selectedNews.value = news
                                            }
                                        )
                                        if (index < activeNewsList.lastIndex) {
                                            HorizontalDivider(
                                                modifier = Modifier.fillMaxWidth(),
                                                thickness = 1.dp,
                                                color = Color.LightGray
                                            )
                                        }
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(.65f)
                                    .padding(top = 10.dp, start = 15.dp)
                            ) {
                                SelectedNewsUI(selectedNews = selectedNews)
                            }
                        }
                    }
                }
            }
        }

        if (showNewsSheet.value) {
            NewsSelectedBottomSheetUI(
                sheetState = selectedNewsSheetState,
                showSheet = showNewsSheet,
                selectedNews = selectedNews.value
            )
        }
    }
}

@Composable
fun EmptyNewsList() {
    Box (
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(top = 130.dp, start = 16.dp, end = 16.dp, bottom = 30.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(Res.string.news_label_active_fetch_empty),
            textAlign = TextAlign.Center,
        )
    }
}