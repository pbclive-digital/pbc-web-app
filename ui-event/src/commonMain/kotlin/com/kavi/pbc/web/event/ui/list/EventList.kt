package com.kavi.pbc.web.event.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.event.ui.common.EventItem
import com.kavi.pbc.web.event.ui.common.EventListItem
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.event.ui.admin.manage.EventManageViewModel
import com.kavi.pbc.web.event.ui.common.RecurringEventListItem
import com.kavi.pbc.web.parent.navigation.EventPath
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_label_past
import pbcwebapp.ui_event.generated.resources.event_label_recurring
import pbcwebapp.ui_event.generated.resources.event_label_upcoming
import pbcwebapp.ui_event.generated.resources.event_phrase_empty_past_events
import pbcwebapp.ui_event.generated.resources.event_phrase_empty_recurring_events
import pbcwebapp.ui_event.generated.resources.event_phrase_empty_upcoming_events
import pbcwebapp.ui_event.generated.resources.event_phrase_note_for_events
import pbcwebapp.ui_event.generated.resources.event_phrase_past
import pbcwebapp.ui_event.generated.resources.event_phrase_recurring
import pbcwebapp.ui_event.generated.resources.event_phrase_upcoming

@Composable
fun EventListUI(navController: NavController) {

    val viewModel: EventListViewModel = viewModel { EventListViewModel() }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxWidth = this.maxWidth

        when (UIUtil.screenType(maxWidth)) {
            ScreenType.PHONE -> EventListPhoneUI(
                viewModel = viewModel,
                navController = navController,
                maxWidth = maxWidth
            )
            ScreenType.TABLET, ScreenType.COMPUTER -> EventListWebUI(
                viewModel = viewModel,
                navController = navController,
                maxWidth = maxWidth
            )
        }
    }
}

@Composable
private fun EventListPhoneUI(viewModel: EventListViewModel, navController: NavController, maxWidth: Dp) {
    val upcomingEventList by viewModel.upcomingEventList.collectAsState()
    val pastEventList by viewModel.pastEventList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUpcomingEvents()
        viewModel.fetchPastEventsWithLimit()
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier
                .padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
            text = stringResource(Res.string.event_label_upcoming),
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold,
            fontFamily = PBCFontFamily,
            fontSize = 22.sp
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            upcomingEventList.forEach { event ->
                EventItem(screenMaxWidth = maxWidth, event = event, onClick = {
                    /**
                     * Alternative way to do the same navigation as a path
                     * navController.navigate("event/event-selected/${event.id}")
                     */
                    navController.navigate(EventPath.EventDetails(eventId = event.id!!))
                })
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider(
            modifier = Modifier.padding(2.dp),
            thickness = 2.dp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            modifier = Modifier
                .padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
            text = stringResource(Res.string.event_label_past),
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold,
            fontFamily = PBCFontFamily,
            fontSize = 22.sp
        )

        Row (
            modifier = Modifier.height(600.dp)
        ) {
            val columCount = when (UIUtil.screenType(maxWidth)) {
                ScreenType.PHONE -> 1
                ScreenType.TABLET -> 2
                ScreenType.COMPUTER -> 3
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(columCount),
            ) {
                items(pastEventList) { event ->
                    EventListItem(
                        event = event,
                        modifier = Modifier.clickable {
                            /**
                             * Alternative way to do the same navigation as a path
                             * navController.navigate("event/event-selected/${event.id}")
                             */
                            navController.navigate(EventPath.EventDetails(eventId = event.id!!))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EventListWebUI(viewModel: EventListViewModel, navController: NavController, maxWidth: Dp) {

    val pastEventList by viewModel.pastEventList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUpcomingEvents()
        viewModel.fetchRecurringEvents()
        viewModel.fetchPastEvents()
    }

    Row (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .weight(.7f)
                .verticalScroll(rememberScrollState())
        ) {
            // This is for upcoming events
            WebUpcomingEvents(
                navController = navController,
                viewModel = viewModel,
                maxWidth = maxWidth
            )

            // This is for recurring events (weekly/monthly)
            WebRecurringEvents(viewModel = viewModel)

            Text(
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                text = stringResource(Res.string.event_phrase_note_for_events),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Thin,
                fontFamily = PBCFontFamily,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column (
            modifier = Modifier.weight(.3f)
        ) {
            WebPastEvents(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun WebUpcomingEvents(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: EventListViewModel,
    maxWidth: Dp
) {
    val upcomingEventList by viewModel.upcomingEventList.collectAsState()

    Column (modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
            text = stringResource(Res.string.event_label_upcoming),
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold,
            fontFamily = PBCFontFamily,
            fontSize = 40.sp
        )

        Text(
            modifier = Modifier
                .padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
            text = stringResource(Res.string.event_phrase_upcoming),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Light,
            fontFamily = PBCFontFamily,
            fontSize = 22.sp
        )

        val columCount = when (UIUtil.screenType(maxWidth)) {
            ScreenType.PHONE -> 0 // This will not consider in this case
            ScreenType.TABLET -> 2
            ScreenType.COMPUTER -> 3
        }

        if (upcomingEventList.isNotEmpty()) {
            FlowRow(
                maxItemsInEachRow = columCount,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center
            ) {
                upcomingEventList.forEach { event ->
                    EventItem(screenMaxWidth = maxWidth, event = event, onClick = {
                        /**
                         * Alternative way to do the same navigation as a path
                         * navController.navigate("event/event-selected/${event.id}")
                         */
                        navController.navigate(EventPath.EventDetails(eventId = event.id!!))
                    })
                }
            }
        } else {
            NoEventsView(
                modifier = Modifier.padding(top = 16.dp),
                message = stringResource(Res.string.event_phrase_empty_upcoming_events)
            )
        }
    }
}

@Composable
private fun WebRecurringEvents(
    modifier: Modifier = Modifier,
    viewModel: EventListViewModel
) {

    val recurringEventList by viewModel.recurringEventList.collectAsState()

    Column (
        modifier = modifier
            .padding(top = 12.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
            text = stringResource(Res.string.event_label_recurring),
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold,
            fontFamily = PBCFontFamily,
            fontSize = 40.sp
        )

        Text(
            modifier = Modifier
                .padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
            text = stringResource(Res.string.event_phrase_recurring),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Light,
            fontFamily = PBCFontFamily,
            fontSize = 22.sp
        )

        if (recurringEventList.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, bottom = 12.dp, end = 8.dp),
            ) {
                recurringEventList.forEachIndexed { index, event ->
                    RecurringEventListItem(event = event) {

                    }
                    if (index < recurringEventList.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                    }
                }
            }
        } else {
            NoEventsView(
                modifier = Modifier.padding(top = 16.dp),
                message = stringResource(Res.string.event_phrase_empty_recurring_events)
            )
        }
    }
}

@Composable
private fun WebPastEvents(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: EventListViewModel
) {
    val pastEventList by viewModel.pastEventList.collectAsState()

    Column (modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
            text = stringResource(Res.string.event_label_past),
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Normal,
            fontFamily = PBCFontFamily,
            fontSize = 28.sp
        )

        Text(
            modifier = Modifier
                .padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
            text = stringResource(Res.string.event_phrase_past),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Light,
            fontFamily = PBCFontFamily,
            fontSize = 18.sp
        )

        if (pastEventList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.padding(top = 8.dp),
            ) {
                items(items = pastEventList) { event ->
                    EventListItem(
                        event = event,
                        modifier = Modifier.clickable {
                            /**
                             * Alternative way to do the same navigation as a path
                             * navController.navigate("event/event-selected/${event.id}")
                             */
                            navController.navigate(EventPath.EventDetails(eventId = event.id!!))
                        }
                    )
                }
            }
        } else {
            NoEventsView(
                modifier = Modifier.padding(top = 16.dp),
                message = stringResource(Res.string.event_phrase_empty_past_events)
            )
        }
    }
}

@Composable
private fun NoEventsView(modifier: Modifier = Modifier, message: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = message,
                fontFamily = PBCFontFamily,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}