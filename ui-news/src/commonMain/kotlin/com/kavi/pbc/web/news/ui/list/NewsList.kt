package com.kavi.pbc.web.news.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kavi.pbc.web.common.ui.component.AppFullScreenLoader
import com.kavi.pbc.web.common.ui.component.PageContainer
import com.kavi.pbc.web.common.ui.model.ProfileActionConfig
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.news.News
import com.kavi.pbc.web.news.data.model.NewsListUiState
import com.kavi.pbc.web.news.ui.common.NewsItem
import com.kavi.pbc.web.news.ui.sheet.NewsSelectedBottomSheetUI
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.data.auth.AppAuthStatus
import com.kavi.pbc.web.datastore.AppLocalStore
import com.kavi.pbc.web.datastore.DataKey
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import com.kavi.pbc.web.parent.extention.openUrl
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_news.generated.resources.Res
import pbcwebapp.ui_news.generated.resources.icon_news_pbc
import pbcwebapp.ui_news.generated.resources.label_news_active_fetch_empty
import pbcwebapp.ui_news.generated.resources.label_news_reference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListUI(navController: NavController, isContainerRequired: Boolean = false) {

    val viewModel: NewsListViewModel = viewModel { NewsListViewModel() }

    LaunchedEffect(Unit) {
        viewModel.fetchAppAuthStatus()
    }

    val appAuthStatus by viewModel.appAuthStatus.collectAsState()

    val showSignUpDialog = remember { mutableStateOf(false) }

    if (isContainerRequired) {
        PageContainer(
            profileActionConfig = ProfileActionConfig(
                appAuthStatus = appAuthStatus,
                profileUserImageUrl = Session.user?.profilePicUrl,
                onProfileClick = {
                    if (Session.isLogIn()) {
                        // Navigate to profile screen
                        println("Profile Tap")
                    }
                },
                onSignOutClick = {
                    // Invoke user authentication
                    if (Session.isLogIn()) {
                        ContractServiceLocator.locate(AuthContract::class).signOut()
                        viewModel.updateAuthStatus(AppAuthStatus.NONE)
                    }
                },
                onSignUpClick = {
                    // Navigate to register screen
                    showSignUpDialog.value = true
                },
                onSignInClick = {
                    // Invoke sign-in with Firebase-Google
                    ContractServiceLocator.locate(AuthContract::class).signInWithFirebaseGoogle()
                    ContractServiceLocator.locate(AuthContract::class).retrieveCurrentAuthStatus { authStatus ->
                        viewModel.updateAuthStatus(authStatus)
                        if (authStatus == AppAuthStatus.SIGN_UP_REQUIRED) {
                            // Navigate to register screen
                            showSignUpDialog.value = true
                        } else {
                            // Re-login and update auth status
                            AppLocalStore.shared.storeValue(DataKey.APP_USER_AUTH_STATUS, authStatus)
                        }
                    }
                }
            )
        ) {
            PageContent(viewModel = viewModel)
        }
    } else {
        PageContent(viewModel = viewModel)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PageContent(viewModel: NewsListViewModel) {
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
                                SelectedNews(selectedNews = selectedNews)
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
private fun SelectedNews(selectedNews: MutableState<News>) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(40.dp)
    ) {
        Column (
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = selectedNews.value.title,
                fontFamily = PBCFontFamily,
                fontSize = 36.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Text(
                text = "on ${selectedNews.value.getFormatPublishedDate()}",
                fontFamily = PBCFontFamily,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Text(
                text = "by ${selectedNews.value.author.name}",
                fontFamily = PBCFontFamily,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(2.dp),
                thickness = 2.dp
            )

            Column {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .shadow(
                                elevation = 12.dp,
                                spotColor = themeAdditionalColors.shadow
                            ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        AsyncImage(
                            model = selectedNews.value.newsImage,
                            error = painterResource(Res.drawable.icon_news_pbc),
                            contentDescription = null, // decorative image
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.background)
                        )
                    }
                }

                Text(
                    text = selectedNews.value.content,
                    fontFamily = PBCFontFamily,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )

                selectedNews.value.facebookLink?.let {
                    Text(
                        text = stringResource(Res.string.label_news_reference),
                        fontFamily = PBCFontFamily,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Justify,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )

                    Text(
                        text = it,
                        fontFamily = PBCFontFamily,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        textDecoration = TextDecoration.Underline,
                        color = themeAdditionalColors.quaternary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp)
                            .clickable {
                                openUrl(url = it)
                            }
                    )
                }
            }
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
            text = stringResource(Res.string.label_news_active_fetch_empty),
            textAlign = TextAlign.Center,
        )
    }
}