package com.kavi.pbc.web.dashboard.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kavi.pbc.web.common.ui.component.AppButtonWithIcon
import com.kavi.pbc.web.common.ui.component.AppLinkButton
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.dashboard.data.model.NewsUiState
import com.kavi.pbc.web.dashboard.ui.common.DashboardEventItem
import com.kavi.pbc.web.dashboard.ui.common.DashboardNewsItem
import com.kavi.pbc.web.data.news.News
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_dashboard.generated.resources.Res
import pbcwebapp.ui_dashboard.generated.resources.icon_facebook
import pbcwebapp.ui_dashboard.generated.resources.icon_youtube
import pbcwebapp.ui_dashboard.generated.resources.image_buddha
import pbcwebapp.ui_dashboard.generated.resources.image_lotus
import pbcwebapp.ui_dashboard.generated.resources.image_pagoda
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_about_us
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_call_us
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_contact_us
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_email_us
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_follow_us
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_no_news
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_news
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_news_more
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_visit_us
import pbcwebapp.ui_dashboard.generated.resources.label_dashboard_visit_us_address
import pbcwebapp.ui_dashboard.generated.resources.phrase_dashboard_about_us

@Composable
fun HomeUI(modifier: Modifier = Modifier, navController: NavController) {

    val viewModel: HomeViewModel = viewModel { HomeViewModel() }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxHeight = this.maxHeight

        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row {
                Column (
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    DailyQuoteAnimatorComponent(maxHeight, viewModel)

                    DashboardEventSliderComponent(viewModel = viewModel, navController =  navController)
                }
                Column (
                    modifier = Modifier.weight(.5f)
                ) {
                    NewsColum(viewModel = viewModel, navController = navController)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                modifier = Modifier.padding(2.dp),
                thickness = 2.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Column (
                    modifier = Modifier.weight(0.6f)
                ) {
                    AboutUs()
                }
                Column (
                    modifier = Modifier.weight(0.4f)
                ) {
                    ContactUs()
                }
            }
        }
    }
}

@Composable
private fun DailyQuoteAnimatorComponent(maxHeight: Dp, viewModel: HomeViewModel,
                                        intervalMillis: Long = 10000L) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var visible by remember { mutableStateOf(true) }
    val dashboardQuoteList by viewModel.dashboardQuoteList.collectAsState()
    val imageList = listOf(
        Res.drawable.image_lotus, Res.drawable.image_buddha, Res.drawable.image_pagoda
    )

    LaunchedEffect(Unit) {
        viewModel.fetchDashboardQuotesFromRemote()
    }

    if (dashboardQuoteList.isNotEmpty()) {
        // Auto-cycle through the list
        LaunchedEffect(currentIndex) {
            visible = true
            delay(intervalMillis)
            visible = false
            delay(500) // wait for exit animation
            currentIndex = (currentIndex + 1) % dashboardQuoteList.size
        }

        BoxWithConstraints (
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight / 6)
                .padding(top = 20.dp, start = 12.dp, end = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            val screenWidth = this.maxWidth
            val componentWidth = (screenWidth.value * 0.6).dp

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column (
                    modifier = Modifier.width(componentWidth)
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .width(60.dp)
                                .height(60.dp),
                            painter = painterResource(imageList[currentIndex]),
                            contentDescription = "Lotus image"
                        )

                        Text(
                            modifier = Modifier
                                .padding(start = 8.dp),
                            text = dashboardQuoteList[currentIndex].quote,
                            textAlign = TextAlign.Justify,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "-- ${dashboardQuoteList[currentIndex].author}",
                        textAlign = TextAlign.End,
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardEventSliderComponent(intervalMillis: Long = 60000L,
                                          viewModel: HomeViewModel,
                                          navController: NavController) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var visible by remember { mutableStateOf(true) }

    val dashboardEvents by viewModel.dashboardEventList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDashboardEvents()
    }

    if (dashboardEvents.isNotEmpty()) {
        // Auto-cycle through the list
        LaunchedEffect(currentIndex) {
            visible = true
            delay(intervalMillis)
            visible = false
            delay(500) // wait for exit animation
            currentIndex = (currentIndex + 1) % dashboardEvents.size
        }

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 12.dp, end = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                DashboardEventItem(
                    event = dashboardEvents[currentIndex],
                    onClick = {

                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsColum(navController: NavController, viewModel: HomeViewModel) {
    val newsUiState by viewModel.newUIStatus.collectAsState()
    val dashboardNews by viewModel.dashboardNewsList.collectAsState()

    val selectedNewsSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val showNewsSheet = remember { mutableStateOf(false) }

    var selectedNews by remember { mutableStateOf(News()) }

    LaunchedEffect(Unit) {
        viewModel.getDashboardNews()
    }

    Column {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = stringResource(Res.string.label_dashboard_news),
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )

        Box {
            when(newsUiState) {
                NewsUiState.NONE -> { /* No implementation */}
                NewsUiState.PENDING -> {
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                NewsUiState.SUCCESS -> {
                    Column {
                        dashboardNews.forEachIndexed { index,  news ->
                            DashboardNewsItem(news = news) {
                                showNewsSheet.value = true
                                selectedNews = news
                            }
                            if (index < dashboardNews.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    thickness = 1.dp,
                                    color = Color.LightGray
                                )
                            }
                        }

                        AppLinkButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, bottom = 20.dp),
                            label = stringResource(Res.string.label_dashboard_news_more),
                            color = MaterialTheme.colorScheme.secondary,
                        ) {
                            navController.navigate("dashboard/to/news/list")
                        }
                    }
                }
                NewsUiState.FAILURE -> {
                    Box (
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(12.dp),
                            text = stringResource(Res.string.label_dashboard_no_news),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AboutUs() {
    Column (
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = stringResource(Res.string.label_dashboard_about_us),
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Text(
            text = stringResource(Res.string.phrase_dashboard_about_us),
            fontFamily = PBCFontFamily,
            fontSize = 16.sp,
            textAlign = TextAlign.Justify,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )
    }
}

@Composable
fun ContactUs() {
    Column (
        modifier = Modifier.padding(top = 8.dp, end = 8.dp, bottom = 8.dp, start = 30.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(Res.string.label_dashboard_contact_us),
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        AppLinkButton(
            modifier = Modifier.padding(top = 14.dp),
            label = stringResource(Res.string.label_dashboard_call_us)
        ) {
            // Open Dialer to call to the number
        }

        AppLinkButton(
            modifier = Modifier.padding(top = 14.dp),
            label = stringResource(Res.string.label_dashboard_email_us)
        ) {
            // Open Email app to compose
        }

        Text(
            modifier = Modifier
                .padding(top = 30.dp),
            text = stringResource(Res.string.label_dashboard_visit_us),
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        AppLinkButton(
            modifier = Modifier.padding(top = 14.dp),
            label = stringResource(Res.string.label_dashboard_visit_us_address)
        ) {
            // Open Email app to compose
        }

        Text(
            modifier = Modifier
                .padding(top = 30.dp),
            text = stringResource(Res.string.label_dashboard_follow_us),
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(Res.drawable.icon_facebook),
                contentDescription = "Facebook icon",
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
                    .clickable {
                        //openLink("https://www.facebook.com/PittsburghBuddhistCenter", context)
                    }
            )

            Image(
                painter = painterResource(Res.drawable.icon_youtube),
                contentDescription = "Youtube icon",
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp)
                    .clickable {
                        //openLink("https://www.youtube.com/pbclive", context)
                    }
            )
        }
    }
}