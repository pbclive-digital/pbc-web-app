package com.kavi.pbc.web.event.ui.admin.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kavi.pbc.web.common.ui.component.AppButtonWithIcon
import com.kavi.pbc.web.common.ui.component.AppIconButton
import com.kavi.pbc.web.common.ui.component.AppTooltipWrap
import com.kavi.pbc.web.common.ui.component.TitleWithActionComposable
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.event.VenueType
import com.kavi.pbc.web.data.event.potluck.PotluckItem
import com.kavi.pbc.web.data.event.signup.SignUpSheet
import com.kavi.pbc.web.event.data.model.EventManageMode
import com.kavi.pbc.web.event.data.model.EventManageOrCreate
import com.kavi.pbc.web.event.ui.admin.common.AgendaItem
import com.kavi.pbc.web.event.ui.admin.common.AgendaItemUI
import com.kavi.pbc.web.event.ui.admin.manage.dialog.DeleteConfirmationDialog
import com.kavi.pbc.web.event.ui.admin.manage.dialog.PublishConfirmationDialog
import com.kavi.pbc.web.event.ui.common.EventItemForAdmin
import com.kavi.pbc.web.network.Network
import com.kavi.pbc.web.parent.extention.openMaps
import com.kavi.pbc.web.parent.extention.openUrl
import kotlinx.browser.window
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_icon_download
import pbcwebapp.ui_event.generated.resources.event_icon_event
import pbcwebapp.ui_event.generated.resources.event_icon_location
import pbcwebapp.ui_event.generated.resources.event_icon_online_meeting
import pbcwebapp.ui_event.generated.resources.event_image_pbc
import pbcwebapp.ui_event.generated.resources.event_label_active
import pbcwebapp.ui_event.generated.resources.event_label_agenda
import pbcwebapp.ui_event.generated.resources.event_label_at
import pbcwebapp.ui_event.generated.resources.event_label_create
import pbcwebapp.ui_event.generated.resources.event_label_draft
import pbcwebapp.ui_event.generated.resources.event_label_from
import pbcwebapp.ui_event.generated.resources.event_label_manage
import pbcwebapp.ui_event.generated.resources.event_label_no_active
import pbcwebapp.ui_event.generated.resources.event_label_no_draft
import pbcwebapp.ui_event.generated.resources.event_label_no_recurring_admin
import pbcwebapp.ui_event.generated.resources.event_label_on
import pbcwebapp.ui_event.generated.resources.event_label_potluck_in_admin
import pbcwebapp.ui_event.generated.resources.event_label_recurring_admin
import pbcwebapp.ui_event.generated.resources.event_label_reg_in_admin
import pbcwebapp.ui_event.generated.resources.event_label_reg_in_admin_seat_count
import pbcwebapp.ui_event.generated.resources.event_label_sign_up_sheet_in_admin
import pbcwebapp.ui_event.generated.resources.event_label_tip_download_potluck_list
import pbcwebapp.ui_event.generated.resources.event_label_tip_download_reg_list
import pbcwebapp.ui_event.generated.resources.event_label_tip_download_signup_list
import pbcwebapp.ui_event.generated.resources.event_label_tip_location
import pbcwebapp.ui_event.generated.resources.event_label_tip_open_meeting
import pbcwebapp.ui_event.generated.resources.event_phrase_agenda
import pbcwebapp.ui_event.generated.resources.event_phrase_agenda_admin
import pbcwebapp.ui_event.generated.resources.event_phrase_manage
import pbcwebapp.ui_event.generated.resources.event_phrase_potluck_in_admin
import pbcwebapp.ui_event.generated.resources.event_phrase_reg_in_admin
import pbcwebapp.ui_event.generated.resources.event_phrase_sign_up_sheet_in_admin
import kotlin.collections.forEachIndexed
import kotlin.js.ExperimentalWasmJsInterop

@Composable
fun EventManageUI(
    navController: NavController,
    eventManageOrCreate: MutableState<EventManageOrCreate>,
    selectedEventForModify: MutableState<Event?>
) {

    val viewModel: EventManageViewModel = viewModel { EventManageViewModel() }

    val selectedEvent = remember { mutableStateOf(Event()) }
    val isInitialEventSelected = remember { mutableStateOf(false) }

    val eventManageMode = remember { mutableStateOf(EventManageMode.UNSELECTED) }

    val showPublishConfirmationDialog = remember { mutableStateOf(false) }
    val publishingEventId = remember { mutableStateOf("") }

    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }
    val deletingEventId = remember { mutableStateOf("") }

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
                titleText = stringResource(Res.string.event_label_manage)
            ) {
                AppButtonWithIcon(
                    modifier = Modifier.padding(bottom = 12.dp),
                    label = stringResource(Res.string.event_label_create),
                    icon = painterResource(Res.drawable.event_icon_event),
                    cornerRadius = 12.dp
                ) {
                    // Navigate to Event-Create
                    selectedEventForModify.value = null
                    eventManageOrCreate.value = EventManageOrCreate.CREATE
                }
            }

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 20.dp)
            ) {
                Text(
                    text = stringResource(Res.string.event_phrase_manage),
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
                        DraftEventBlock(
                            viewModel = viewModel,
                            isSelected = isInitialEventSelected,
                            publishConfirmation = showPublishConfirmationDialog,
                            publishingId = publishingEventId,
                            deleteConfirmation = showDeleteConfirmationDialog,
                            deletingId = deletingEventId,
                            eventManageOrCreate = eventManageOrCreate,
                            selectedForModify = selectedEventForModify,
                            eventMode = eventManageMode
                        ) { event ->
                            selectedEvent.value = event
                        }

                        RecurringEventBlock(
                            viewModel = viewModel,
                            isSelected = isInitialEventSelected,
                            deleteConfirmation = showDeleteConfirmationDialog,
                            deletingId = deletingEventId,
                            eventManageOrCreate = eventManageOrCreate,
                            selectedForModify = selectedEventForModify,
                            eventMode = eventManageMode
                        ) { event ->
                            selectedEvent.value = event
                        }

                        ActiveEventBlock(
                            viewModel = viewModel,
                            isSelected = isInitialEventSelected,
                            deleteConfirmation = showDeleteConfirmationDialog,
                            deletingId = deletingEventId,
                            eventManageOrCreate = eventManageOrCreate,
                            selectedForModify = selectedEventForModify,
                            eventMode = eventManageMode
                        ) { event ->
                            selectedEvent.value = event
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column (modifier = Modifier.weight(.65f)) {
                        SelectedEventUI(
                            selectedEvent = selectedEvent
                        )
                    }
                }
            }
        }
    }

    if (showPublishConfirmationDialog.value) {
        PublishConfirmationDialog(
            showDialog = showPublishConfirmationDialog,
            onAgree = {
                showPublishConfirmationDialog.value = false
                viewModel.publishDraftEvent(eventId = publishingEventId.value)
                publishingEventId.value = ""
            },
            onDisagree = {
                showPublishConfirmationDialog.value = false
                publishingEventId.value = ""
            }
        )
    }

    if (showDeleteConfirmationDialog.value) {
        DeleteConfirmationDialog(
            showDialog = showDeleteConfirmationDialog,
            onAgree = {
                showDeleteConfirmationDialog.value = false
                viewModel.deleteEvent(eventId = deletingEventId.value, eventManageMode = eventManageMode.value)
                deletingEventId.value = ""
            },
            onDisagree = {
                showDeleteConfirmationDialog.value = false
                deletingEventId.value = ""
            }
        )
    }
}

@Composable
private fun DraftEventBlock(
    viewModel: EventManageViewModel,
    isSelected: MutableState<Boolean>,
    publishConfirmation: MutableState<Boolean>,
    publishingId: MutableState<String>,
    deleteConfirmation: MutableState<Boolean>,
    deletingId: MutableState<String>,
    eventManageOrCreate: MutableState<EventManageOrCreate>,
    selectedForModify: MutableState<Event?>,
    eventMode: MutableState<EventManageMode>,
    onSelect:(event: Event) -> Unit
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current
    val draftEventList by viewModel.draftEventList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchDraftEvents()
    }

    Text(
        text = stringResource(Res.string.event_label_draft),
        fontFamily = PBCFontFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
    )

    Column {
        if (draftEventList.isNotEmpty()) {
            // Set initial selected Event
            onSelect.invoke(draftEventList[0])
            isSelected.value = true

            draftEventList.forEachIndexed { index, event ->
                EventItemForAdmin(
                    modifier = Modifier.clickable {
                        onSelect.invoke(event)
                    },
                    event = event,
                    isDraftEvent = true,
                    onModify = {
                        selectedForModify.value = event
                        eventManageOrCreate.value = EventManageOrCreate.CREATE
                    },
                    onPublish = {
                        publishConfirmation.value = true
                        publishingId.value = event.id!!
                    },
                    onDelete = {
                        deleteConfirmation.value = true
                        eventMode.value = EventManageMode.DRAFT
                        deletingId.value = event.id!!
                    }
                )
                if (index < draftEventList.lastIndex) {
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
                    text = stringResource(Res.string.event_label_no_draft),
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
private fun RecurringEventBlock(
    viewModel: EventManageViewModel,
    isSelected: MutableState<Boolean>,
    deleteConfirmation: MutableState<Boolean>,
    deletingId: MutableState<String>,
    eventManageOrCreate: MutableState<EventManageOrCreate>,
    selectedForModify: MutableState<Event?>,
    eventMode: MutableState<EventManageMode>,
    onSelect:(event: Event) -> Unit
) {

    val themeAdditionalColors = LocalThemeAdditionalColors.current
    val recurringEventList by viewModel.recurringEventList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRecurringEvents()
    }

    Text(
        text = stringResource(Res.string.event_label_recurring_admin),
        fontFamily = PBCFontFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
    )

    Column {
        if (recurringEventList.isNotEmpty()) {
            // Set initial selected Event
            if (!isSelected.value) {
                onSelect.invoke(recurringEventList[0])
                isSelected.value = true
            }

            recurringEventList.forEachIndexed { index, event ->
                EventItemForAdmin(
                    modifier = Modifier.clickable {
                        onSelect.invoke(event)
                    },
                    event = event,
                    isDraftEvent = false,
                    onModify = {
                        selectedForModify.value = event
                        eventManageOrCreate.value = EventManageOrCreate.CREATE
                    },
                    onPublish = {
                        /* Nothing to implement */
                    },
                    onDelete = {
                        deleteConfirmation.value = true
                        eventMode.value = EventManageMode.RECURRING
                        deletingId.value = event.id!!
                    }
                )
                if (index < recurringEventList.lastIndex) {
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
                    text = stringResource(Res.string.event_label_no_recurring_admin),
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
private fun ActiveEventBlock(
    viewModel: EventManageViewModel,
    isSelected: MutableState<Boolean>,
    deleteConfirmation: MutableState<Boolean>,
    deletingId: MutableState<String>,
    eventManageOrCreate: MutableState<EventManageOrCreate>,
    selectedForModify: MutableState<Event?>,
    eventMode: MutableState<EventManageMode>,
    onSelect:(event: Event) -> Unit
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current
    val activeEventList by viewModel.activeEventList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchActiveEvents()
    }

    Text(
        text = stringResource(Res.string.event_label_active),
        fontFamily = PBCFontFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
    )

    Column {
        if (activeEventList.isNotEmpty()) {
            // Set initial selected Event
            if (!isSelected.value) {
                onSelect.invoke(activeEventList[0])
                isSelected.value = true
            }

            activeEventList.forEachIndexed { index, event ->
                EventItemForAdmin(
                    modifier = Modifier.clickable {
                        onSelect.invoke(event)
                    },
                    event = event,
                    isDraftEvent = false,
                    onModify = {
                        selectedForModify.value = event
                        eventManageOrCreate.value = EventManageOrCreate.CREATE
                    },
                    onPublish = {
                        /* Nothing to implement */
                    },
                    onDelete = {
                        deleteConfirmation.value = true
                        eventMode.value = EventManageMode.ACTIVE
                        deletingId.value = event.id!!
                    }
                )
                if (index < activeEventList.lastIndex) {
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
                    text = stringResource(Res.string.event_label_no_active),
                    textAlign = TextAlign.Center,
                    fontFamily = PBCFontFamily,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
private fun SelectedEventUI(selectedEvent: MutableState<Event>) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current
    val viewModel: EventManageViewModel = viewModel { EventManageViewModel() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(40.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Row {
                Card(
                    modifier = Modifier
                        .background(Color.Transparent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    AsyncImage(
                        model = selectedEvent.value.eventImage,
                        error = painterResource(Res.drawable.event_image_pbc),
                        contentDescription = null, // decorative image
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(color = MaterialTheme.colorScheme.background)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = selectedEvent.value.name,
                        fontFamily = PBCFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp,
                        lineHeight = 40.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = selectedEvent.value.description,
                        fontFamily = PBCFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Row (
                        modifier = Modifier.padding(top = 16.dp),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = stringResource(Res.string.event_label_on),
                                fontFamily = PBCFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Text(
                                modifier = Modifier.padding(top = 8.dp),
                                text = selectedEvent.value.getFormatDate(),
                                fontFamily = PBCFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = stringResource(Res.string.event_label_from),
                                fontFamily = PBCFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Text(
                                modifier = Modifier.padding(top = 8.dp),
                                text = "${selectedEvent.value.startTime} - ${selectedEvent.value.endTime}",
                                fontFamily = PBCFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Row (
                        modifier = Modifier.padding(top = 16.dp),
                    ) {
                        if (selectedEvent.value.venueType == VenueType.PHYSICAL) {
                            Text(
                                text = stringResource(Res.string.event_label_at),
                                fontFamily = PBCFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Text(
                                modifier = Modifier.padding(start = 4.dp),
                                text = selectedEvent.value.getPlace(),
                                fontFamily = PBCFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            AppTooltipWrap(
                                tipLabel = stringResource(Res.string.event_label_tip_location)
                            ) {
                                AppIconButton(
                                    icon = painterResource(Res.drawable.event_icon_location),
                                    buttonSize = 40.dp
                                ) {
                                    selectedEvent.value.venueAddress?.let {
                                        val addressUrl = it.replace(" ", "+")
                                        val locationUrl =
                                            "https://www.google.com/maps/place/$addressUrl"
                                        openMaps(mapUrl = locationUrl)
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = selectedEvent.value.getPlace(),
                                fontFamily = PBCFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            AppTooltipWrap(
                                tipLabel = stringResource(Res.string.event_label_tip_open_meeting)
                            ) {
                                AppIconButton(
                                    icon = painterResource(Res.drawable.event_icon_online_meeting),
                                    buttonSize = 40.dp
                                ) {
                                    selectedEvent.value.meetingUrl?.let {
                                        openUrl(url = it)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedEvent.value.agendaAvailable) {
                Column (
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.event_label_agenda),
                        fontFamily = PBCFontFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(2.dp),
                        thickness = 2.dp
                    )

                    Text(
                        text = stringResource(Res.string.event_phrase_agenda_admin),
                        fontFamily = PBCFontFamily,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    selectedEvent.value.agendaItemList?.let { itemList ->
                        itemList.forEachIndexed { index, agendaItem ->
                            AgendaItemUI(modifier = Modifier.padding(8.dp), agendaItem = agendaItem)
                            if (index < itemList.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 8.dp),
                                    thickness = 1.dp,
                                    color = themeAdditionalColors.shadow
                                )
                            }
                        }
                    }
                }
            }

            if (selectedEvent.value.registrationRequired) {
                Column (
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Row {
                        Text(
                            text = stringResource(Res.string.event_label_reg_in_admin),
                            fontFamily = PBCFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        AppTooltipWrap(
                            tipLabel = stringResource(Res.string.event_label_tip_download_reg_list)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.event_icon_download),
                                contentDescription = "Download .csv",
                                tint = themeAdditionalColors.shadow,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(4.dp)
                                    .clickable {
                                        // Download event registration .csv
                                        viewModel.downloadEventRegistrationList(eventId = selectedEvent.value.id!!) { urlPath ->
                                            val downloadLink =
                                                "${Network.shared.getBaseUrl()}$urlPath"
                                            window.open(url = downloadLink, "_blank")
                                        }
                                    }
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(2.dp),
                        thickness = 2.dp
                    )

                    Text(
                        text = stringResource(Res.string.event_phrase_reg_in_admin) +
                                " ${stringResource(Res.string.event_label_reg_in_admin_seat_count)}: " +
                                "${selectedEvent.value.openSeatCount}",
                        fontFamily = PBCFontFamily,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            if (selectedEvent.value.potluckAvailable) {
                Column (
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Row {
                        Text(
                            text = stringResource(Res.string.event_label_potluck_in_admin),
                            fontFamily = PBCFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        AppTooltipWrap(
                            tipLabel = stringResource(Res.string.event_label_tip_download_potluck_list)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.event_icon_download),
                                contentDescription = "Download .csv",
                                tint = themeAdditionalColors.shadow,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(4.dp)
                                    .clickable {
                                        // Download potluck contribution .csv
                                        viewModel.downloadEventPotluckContribution(eventId = selectedEvent.value.id!!) { urlPath ->
                                            val downloadLink =
                                                "${Network.shared.getBaseUrl()}$urlPath"
                                            window.open(url = downloadLink, "_blank")
                                        }
                                    }
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(2.dp),
                        thickness = 2.dp
                    )

                    Text(
                        text = stringResource(Res.string.event_phrase_potluck_in_admin),
                        fontFamily = PBCFontFamily,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    selectedEvent.value.potluckItemList?.let { potluckItems ->
                        FlowRow(
                            maxItemsInEachRow = 3,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            potluckItems.forEach { item ->
                                PotluckItemUI(
                                    modifier = Modifier.padding(4.dp),
                                    potluckItem = item
                                )
                            }
                        }
                    }
                }
            }

            if (selectedEvent.value.signUpSheetAvailable) {
                Column (
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.event_label_sign_up_sheet_in_admin),
                        fontFamily = PBCFontFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(2.dp),
                        thickness = 2.dp
                    )

                    Text(
                        text = stringResource(Res.string.event_phrase_sign_up_sheet_in_admin),
                        fontFamily = PBCFontFamily,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    selectedEvent.value.signUpSheetList?.let { signUpSheets ->
                        signUpSheets.forEach { sheet ->
                            Spacer(modifier = Modifier.height(8.dp))
                            SignUpSheetItem(signUpSheet = sheet) {
                                // Download potluck contribution .csv
                                viewModel.downloadEventSignUpSheetContribution(
                                    eventId = selectedEvent.value.id!!,
                                    sheetId = sheet.sheetId
                                ) { urlPath ->
                                    val downloadLink = "${Network.shared.getBaseUrl()}$urlPath"
                                    window.open(url = downloadLink, "_blank")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PotluckItemUI(
    modifier: Modifier = Modifier,
    potluckItem: PotluckItem
) {
    Row (
        modifier = modifier
            .width(300.dp)
            .border(1.dp, MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(8.dp))
            .clip( RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.Top
    ) {
        Row (
          modifier = Modifier
              .background(MaterialTheme.colorScheme.surface)
              .padding(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .weight(.85f),
                text = potluckItem.itemName,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = PBCFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                    .weight(.15f),
                text = "${potluckItem.itemCount}",
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = PBCFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun SignUpSheetItem(modifier: Modifier = Modifier, signUpSheet: SignUpSheet, onDownload: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(8.dp))
            .clip( RoundedCornerShape(8.dp))
            .shadow(elevation = 2.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp),
                text = signUpSheet.sheetName,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = PBCFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp),
                text = signUpSheet.sheetDescription,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = PBCFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Thin,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AppTooltipWrap(
            tipLabel = stringResource(Res.string.event_label_tip_download_signup_list)
        ) {
            Icon(
                painter = painterResource(Res.drawable.event_icon_download),
                contentDescription = "Download .csv",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 16.dp)
                    .clickable {
                        // Download potluck contribution .csv
                        onDownload.invoke()
                    }
            )
        }
    }
}