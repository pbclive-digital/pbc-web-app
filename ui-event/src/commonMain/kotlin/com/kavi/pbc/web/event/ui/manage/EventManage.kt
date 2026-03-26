package com.kavi.pbc.web.event.ui.manage

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
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.event.data.model.EventManageMode
import com.kavi.pbc.web.event.ui.common.EventItemForAdmin
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_icon_event
import pbcwebapp.ui_event.generated.resources.event_label_active
import pbcwebapp.ui_event.generated.resources.event_label_create
import pbcwebapp.ui_event.generated.resources.event_label_draft
import pbcwebapp.ui_event.generated.resources.event_label_manage
import pbcwebapp.ui_event.generated.resources.event_label_no_active
import pbcwebapp.ui_event.generated.resources.event_label_no_draft
import pbcwebapp.ui_event.generated.resources.event_phrase_manage

@Composable
fun EventManageUI(navController: NavController) {

    val viewModel: EventManageViewModel = viewModel { EventManageViewModel() }

    val selectedEvent = remember { mutableStateOf(Event()) }
    val isInitialEventSelected = remember { mutableStateOf(false) }

    val eventManageMode = remember { mutableStateOf(EventManageMode.UNSELECTED) }

    val showPublishConfirmationDialog = remember { mutableStateOf(false) }
    val publishingEventId = remember { mutableStateOf("") }

    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }
    val deletingEventId = remember { mutableStateOf("") }

    val showCreateOrModifyDialog = remember { mutableStateOf(false) }
    val selectedEventForModify: MutableState<Event?> = remember { mutableStateOf(null) }

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
                    //showCreateOrModifyDialog.value = true
                }
            }

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 30.dp)
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
                            showModify = showCreateOrModifyDialog,
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
                            showModify = showCreateOrModifyDialog,
                            selectedForModify = selectedEventForModify,
                            eventMode = eventManageMode
                        ) { event ->
                            selectedEvent.value = event
                        }
                    }

                    Column (modifier = Modifier.weight(.65f)) {
                        /*SelectedNewsUI(
                            modifier = Modifier.padding(start = 8.dp),
                            selectedNews = selectedNews
                        )*/
                    }
                }
            }
        }
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
    showModify: MutableState<Boolean>,
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
                        showModify.value = true
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
private fun ActiveEventBlock(
    viewModel: EventManageViewModel,
    isSelected: MutableState<Boolean>,
    deleteConfirmation: MutableState<Boolean>,
    deletingId: MutableState<String>,
    showModify: MutableState<Boolean>,
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
            onSelect.invoke(activeEventList[0])
            isSelected.value = true

            activeEventList.forEachIndexed { index, event ->
                EventItemForAdmin(
                    modifier = Modifier.clickable {
                        onSelect.invoke(event)
                    },
                    event = event,
                    isDraftEvent = false,
                    onModify = {
                        selectedForModify.value = event
                        showModify.value = true
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