package com.kavi.pbc.web.event.ui.admin.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kavi.pbc.web.common.ui.component.AppButtonWithIcon
import com.kavi.pbc.web.common.ui.component.AppDatePickerButton
import com.kavi.pbc.web.common.ui.component.AppDatePickerDialog
import com.kavi.pbc.web.common.ui.component.AppDropDownMenu
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppOutlineMultiLineTextField
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.component.AppTimePickerButton
import com.kavi.pbc.web.common.ui.component.AppTimePickerDialog
import com.kavi.pbc.web.common.ui.component.ErrorMessageBalloon
import com.kavi.pbc.web.common.ui.component.TitleWithBackNav
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.data.event.EventType
import com.kavi.pbc.web.data.event.VenueType
import com.kavi.pbc.web.data.util.DateTimeUtil
import com.kavi.pbc.web.event.data.model.EventCreateOrModifyUiState
import com.kavi.pbc.web.event.data.model.EventManageOrCreate
import com.kavi.pbc.web.event.data.model.TimePickerMode
import com.kavi.pbc.web.event.ui.admin.common.PotluckListItem
import com.kavi.pbc.web.event.ui.admin.common.SignUpSheetListItem
import com.kavi.pbc.web.event.ui.admin.create.dialog.PotluckItemCreateDialog
import com.kavi.pbc.web.event.ui.admin.create.dialog.SignUpSheetCreateDialog
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.util.toImageBitmap
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_icon_add_image
import pbcwebapp.ui_event.generated.resources.event_icon_plus
import pbcwebapp.ui_event.generated.resources.event_label_add_new_sign_up_sheet
import pbcwebapp.ui_event.generated.resources.event_label_add_to_potluck
import pbcwebapp.ui_event.generated.resources.event_label_additional_sign_up_sheets
import pbcwebapp.ui_event.generated.resources.event_label_available_search_count
import pbcwebapp.ui_event.generated.resources.event_label_count
import pbcwebapp.ui_event.generated.resources.event_label_create_new
import pbcwebapp.ui_event.generated.resources.event_label_description
import pbcwebapp.ui_event.generated.resources.event_label_event_date
import pbcwebapp.ui_event.generated.resources.event_label_event_type
import pbcwebapp.ui_event.generated.resources.event_label_is_additional_sign_ups_available
import pbcwebapp.ui_event.generated.resources.event_label_meeting_url
import pbcwebapp.ui_event.generated.resources.event_label_pick_image
import pbcwebapp.ui_event.generated.resources.event_label_potluck_is_potluck_held
import pbcwebapp.ui_event.generated.resources.event_label_potluck_setup_in_admin
import pbcwebapp.ui_event.generated.resources.event_label_registration_in_admin
import pbcwebapp.ui_event.generated.resources.event_label_registration_required
import pbcwebapp.ui_event.generated.resources.event_label_title
import pbcwebapp.ui_event.generated.resources.event_label_update_event
import pbcwebapp.ui_event.generated.resources.event_label_venue
import pbcwebapp.ui_event.generated.resources.event_label_venue_address
import pbcwebapp.ui_event.generated.resources.event_label_venue_type
import pbcwebapp.ui_event.generated.resources.event_label_when
import pbcwebapp.ui_event.generated.resources.event_label_where
import pbcwebapp.ui_event.generated.resources.event_phrase_additional_sign_up_sheets
import pbcwebapp.ui_event.generated.resources.event_phrase_create_or_modify_empty_fields
import pbcwebapp.ui_event.generated.resources.event_phrase_create_or_modify_failure
import pbcwebapp.ui_event.generated.resources.event_phrase_potluck_setup_in_admin
import pbcwebapp.ui_event.generated.resources.event_phrase_registration_in_admin

@Composable
fun EventCreateUI(
    navController: NavController,
    eventManageOrCreate: MutableState<EventManageOrCreate>,
    modifyEvent: Event? = null
) {
    val viewModel: EventCreateViewModel = viewModel { EventCreateViewModel() }
    val isModify = modifyEvent != null

    LaunchedEffect(modifyEvent) {
        if (modifyEvent != null) {
            viewModel.setModifyEvent(modifyEvent)
        } else {
            viewModel.initiateNewEvent()
        }
    }

    val createOrModifyEvent by viewModel.createOrModifyEvent.collectAsState()
    val eventCreationOrModifyState by viewModel.eventCreationOrModifyState.collectAsState()
    val eventFormValidationError by viewModel.eventFormValidationError.collectAsState()

    val errorBalloonVisibility = remember { mutableStateOf(false) }
    var errorBalloonMessage by remember { mutableStateOf("") }

    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    val scope = rememberCoroutineScope()
    val imagePickerLauncher = rememberFilePickerLauncher(
        type = FileKitType.Image,
        title = "Select Image for NEWS"
    ) { platformFile ->
        // Handle the selected file
        platformFile?.let { imageFile ->
            scope.launch {
                // Read the file and convert it to ImageBitmap
                selectedImage = imageFile.toImageBitmap()
                viewModel.updateEventImageFile(imageFile)
            }
        }
    }

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
            TitleWithBackNav(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                titleText = if (isModify) stringResource(Res.string.event_label_update_event) else stringResource(Res.string.event_label_create_new),
                backAction = {
                    eventManageOrCreate.value = EventManageOrCreate.MANAGE
                }
            )

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 20.dp)
            ) {
                Row ( modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max) ) {
                    Column (
                        modifier = Modifier
                            .weight(.7f)
                            .verticalScroll(state = rememberScrollState())
                    ) {
                        // Show error balloon for any creation / update issue
                        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            // Error or Success message balloon
                            ErrorMessageBalloon(
                                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                                showBalloon = errorBalloonVisibility,
                                errorMessage = errorBalloonMessage,
                                onDismiss = {
                                    errorBalloonVisibility.value = false
                                    viewModel.revokeEventCreateOrModifyUiState()
                                }
                            )
                        }

                        // This will contain all text inputs
                        EventCreationForm(viewModel = viewModel)

                        // This will contain UI for set-up event registration
                        EventRegistrationSetup(viewModel = viewModel)

                        // This will contain UI for set-up event potluck
                        EventPotluckSetup(viewModel = viewModel)

                        // This will contain UI for set-up additional sign-up sheets
                        EventSignUpSheetSetup(viewModel = viewModel)

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
                        ) {
                            Spacer(modifier = Modifier.weight(1f))

                            AppFilledButton(
                                label = if (isModify) stringResource(Res.string.event_label_update_event) else stringResource(Res.string.event_label_create_new),
                            ) {
                                viewModel.uploadEventImageAndCreateOrUpdateEvent(isModify = isModify)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column (
                        modifier = Modifier
                            .weight(.3f)
                    ) {
                        // This will have image upload for event
                        Text(
                            text = stringResource(Res.string.event_label_pick_image),
                            fontFamily = PBCFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth()
                        )

                        Box (
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.event_icon_add_image),
                                contentDescription = "Adding news image icon",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clickable {
                                        // Open up image selection from machine
                                        imagePickerLauncher.launch()
                                    }
                            )

                            selectedImage?.let {
                                Box (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        bitmap = it,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .size(250.dp)
                                    )
                                }
                            }?: run {
                                createOrModifyEvent.eventImage?.let {
                                    Box (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AsyncImage(
                                            model = it,
                                            contentDescription = "Profile Picture",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(250.dp)
                                                .padding(5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    when(eventCreationOrModifyState) {
        EventCreateOrModifyUiState.NONE -> {}
        EventCreateOrModifyUiState.PENDING -> {}
        EventCreateOrModifyUiState.EMPTY_FIELD -> {
            errorBalloonVisibility.value = true
            errorBalloonMessage = stringResource(Res.string.event_phrase_create_or_modify_empty_fields) + eventFormValidationError
        }
        EventCreateOrModifyUiState.FAILURE -> {
            errorBalloonVisibility.value = true
            errorBalloonMessage = stringResource(Res.string.event_phrase_create_or_modify_failure)
        }
        EventCreateOrModifyUiState.SUCCESS -> {
            eventManageOrCreate.value = EventManageOrCreate.MANAGE
            viewModel.revokeEventCreateOrModifyUiState()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventCreationForm(viewModel: EventCreateViewModel) {

    val createOrModifyEvent by viewModel.createOrModifyEvent.collectAsState()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = DateTimeUtil.datePickerInitializeMills(),
        //initialDisplayedMonthMillis = DateTimeUtil.datePickerInitializeMonthMills(),
        initialDisplayMode = DisplayMode.Picker
    )

    val showDatePicker = remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var timePickerMode by remember { mutableStateOf(TimePickerMode.UNSELECTED) }

    val eventName = remember { mutableStateOf(TextFieldValue(createOrModifyEvent.name)) }
    val eventDescription = remember { mutableStateOf(TextFieldValue(createOrModifyEvent.description)) }
    val eventType = remember { mutableStateOf(viewModel.getInitialEventType()) }
    val eventDate = remember { mutableStateOf(viewModel.getInitialEventDate()) }
    val eventFrom = remember { mutableStateOf(viewModel.getInitialStartTime()) }
    val eventTo = remember { mutableStateOf(viewModel.getInitialEndTime()) }
    val venueType = remember { mutableStateOf(viewModel.getInitialVenueType()) }
    val eventVenue = remember { mutableStateOf(TextFieldValue(createOrModifyEvent.venue ?: run { "" })) }
    val eventVenueAddress = remember { mutableStateOf(TextFieldValue(createOrModifyEvent.venueAddress ?: run { "" })) }
    val eventMeetingUrl = remember { mutableStateOf(TextFieldValue(createOrModifyEvent.meetingUrl ?: run { "" })) }

    // Sync local state when ViewModel state changes
    LaunchedEffect(createOrModifyEvent) {
        eventName.value = TextFieldValue(createOrModifyEvent.name)
        eventDescription.value = TextFieldValue(createOrModifyEvent.description)
        eventType.value = viewModel.getInitialEventType()
        eventDate.value = viewModel.getInitialEventDate()
        eventFrom.value = viewModel.getInitialStartTime()
        eventTo.value = viewModel.getInitialEndTime()
        venueType.value = viewModel.getInitialVenueType()
        eventVenue.value = TextFieldValue(createOrModifyEvent.venue ?: run { "" })
        eventVenueAddress.value = TextFieldValue(createOrModifyEvent.venueAddress ?: run { "" })
        eventMeetingUrl.value = TextFieldValue(createOrModifyEvent.meetingUrl ?: run { "" })
    }

    // Update viewModel with local changes of event-type
    LaunchedEffect(eventType.value) {
        viewModel.updateEventType(eventType = eventType.value)
    }

    // Update viewModel with local changes of venue-type
    LaunchedEffect(venueType.value) {
        viewModel.updateVenueType(venueType = venueType.value)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        AppOutlineTextField (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            headingText = stringResource(Res.string.event_label_title).uppercase(),
            contentText = eventName,
            onValueChange = { newValue ->
                eventName.value = newValue
                viewModel.updateName(eventName.value.text)
            }
        )

        AppOutlineMultiLineTextField (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .height(200.dp),
            headingText = stringResource(Res.string.event_label_description).uppercase(),
            contentText = eventDescription,
            maxLines = 12,
            onValueChange = { newValue ->
                eventDescription.value = newValue
                viewModel.updateDescription(eventDescription.value.text)
            }
        )

        AppDropDownMenu(
            modifier = Modifier
                .padding(top = 4.dp),
            title = stringResource(Res.string.event_label_event_type).uppercase(),
            selectableItems = listOf(
                EventType.BUDDHISM_CLASS.name, EventType.MEDITATION.name,
                EventType.DHAMMA_TALK.name, EventType.SPECIAL.name),
            selectedItem = eventType,
        )

        Text(
            text = stringResource(Res.string.event_label_when),
            fontFamily = PBCFontFamily,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )

        HorizontalDivider(
            modifier = Modifier.padding(2.dp),
            thickness = 2.dp
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 4.dp, end = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.event_label_event_date),
                fontFamily = PBCFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.weight(1f))

            AppDatePickerButton (
                modifier = Modifier.width(200.dp),
                label = eventDate
            ) {
                showDatePicker.value = true
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 4.dp, end = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTimePickerButton(
                modifier = Modifier
                    .weight(1f)
                    .width(150.dp),
                label = eventFrom
            ) {
                showTimePicker = true
                timePickerMode = TimePickerMode.FROM
            }

            Spacer(modifier = Modifier.width(16.dp))

            AppTimePickerButton(
                modifier = Modifier
                    .weight(1f)
                    .width(150.dp),
                label = eventTo
            ) {
                showTimePicker = true
                timePickerMode = TimePickerMode.TO
            }
        }

        Text(
            text = stringResource(Res.string.event_label_where),
            fontFamily = PBCFontFamily,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )

        HorizontalDivider(
            modifier = Modifier.padding(2.dp),
            thickness = 2.dp
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 4.dp, end = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.event_label_venue_type),
                fontFamily = PBCFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.weight(1f))

            AppDropDownMenu(
                modifier = Modifier.width(200.dp),
                title = stringResource(Res.string.event_label_venue_type).uppercase(),
                selectableItems = listOf(VenueType.VIRTUAL.name, VenueType.PHYSICAL.name),
                selectedItem = venueType
            )
        }

        when (venueType.value) {
            VenueType.DEFAULT.name -> {}
            VenueType.PHYSICAL.name -> {
                AppOutlineTextField (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    headingText = stringResource(Res.string.event_label_venue).uppercase(),
                    contentText = eventVenue,
                    onValueChange = { newValue ->
                        eventVenue.value = newValue
                        viewModel.updateVenue(eventVenue.value.text)
                    }
                )

                AppOutlineTextField (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    headingText = stringResource(Res.string.event_label_venue_address).uppercase(),
                    contentText = eventVenueAddress,
                    onValueChange = { newValue ->
                        eventVenueAddress.value = newValue
                        viewModel.updateVenueAddress(eventVenueAddress.value.text)
                    }
                )
            }
            VenueType.VIRTUAL.name -> {
                AppOutlineTextField (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    headingText = stringResource(Res.string.event_label_meeting_url).uppercase(),
                    contentText = eventMeetingUrl,
                    onValueChange = { newValue ->
                        eventMeetingUrl.value = newValue
                        viewModel.updateMeetingUrl(eventMeetingUrl.value.text)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }

    if (showDatePicker.value) {
        AppDatePickerDialog (
            showDatePicker = showDatePicker,
            datePickerState = datePickerState,
            onConfirmAction = {
                showDatePicker.value = false
                eventDate.value = viewModel.formatDate(datePickerState.selectedDateMillis)
                viewModel.updateDate(datePickerState.selectedDateMillis)
            },
            onDismissAction = {
                showDatePicker.value = false
            }
        )
    }

    if (showTimePicker) {
        AppTimePickerDialog(
            onConfirm = { hour, minute ->
                showTimePicker = false
                when(timePickerMode) {
                    TimePickerMode.FROM -> {
                        eventFrom.value = viewModel.formatTime(hour = hour, minute = minute)
                        timePickerMode = TimePickerMode.UNSELECTED
                        viewModel.updateStartTime(eventFrom.value)
                    }
                    TimePickerMode.TO -> {
                        eventTo.value = viewModel.formatTime(hour = hour, minute = minute)
                        timePickerMode = TimePickerMode.UNSELECTED
                        viewModel.updateEndTime(eventTo.value)
                    }
                    TimePickerMode.UNSELECTED -> {
                        // Nothing will set
                    }
                }
            },
            onDismiss = { showTimePicker = false }
        )
    }
}

@Composable
private fun EventRegistrationSetup(viewModel: EventCreateViewModel) {

    val createOrModifyEvent by viewModel.createOrModifyEvent.collectAsState()

    var isRegistrationChecked by remember { mutableStateOf(createOrModifyEvent.registrationRequired) }
    val availableSeatCount = remember { mutableStateOf(TextFieldValue(
        createOrModifyEvent.openSeatCount?.toString() ?: run { "" })) }

    LaunchedEffect(createOrModifyEvent) {
        isRegistrationChecked = createOrModifyEvent.registrationRequired
        availableSeatCount.value = TextFieldValue(
            createOrModifyEvent.openSeatCount?.toString() ?: run { "" })
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
    ) {
        Text(
            text = stringResource(Res.string.event_label_registration_in_admin),
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
            text = stringResource(Res.string.event_phrase_registration_in_admin),
            fontFamily = PBCFontFamily,
            fontSize = 16.sp,
            textAlign = TextAlign.Justify,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.event_label_registration_required),
                fontFamily = PBCFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.weight(1f))

            Checkbox(
                checked = isRegistrationChecked,
                onCheckedChange = { newCheckedState ->
                    isRegistrationChecked = newCheckedState
                    viewModel.updateRegistrationRequiredFlag(newCheckedState)
                }
            )
        }

        if (isRegistrationChecked) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.event_label_available_search_count),
                    fontFamily = PBCFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.weight(1f))

                AppOutlineTextField(
                    modifier = Modifier
                        .width(150.dp)
                        .padding(top = 8.dp),
                    headingText = stringResource(Res.string.event_label_count).uppercase(),
                    contentText = availableSeatCount,
                    keyboardType = KeyboardType.Number,
                    onValueChange = { newValue ->
                        availableSeatCount.value = newValue
                        if (newValue.text.isNotEmpty())
                            viewModel.updateSeatCount(newValue.text.toInt())
                    }
                )
            }
        }
    }
}

@Composable
private fun EventPotluckSetup(viewModel: EventCreateViewModel) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current
    val createOrModifyEvent by viewModel.createOrModifyEvent.collectAsState()
    val potluckItemList by viewModel.potluckItemList.collectAsState()

    var isPotluckChecked by remember { mutableStateOf(createOrModifyEvent.potluckAvailable) }
    val showCreatePotluckItemDialog = remember { mutableStateOf(false) }

    LaunchedEffect(createOrModifyEvent) {
        isPotluckChecked = createOrModifyEvent.potluckAvailable
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
    ) {
        Text(
            text = stringResource(Res.string.event_label_potluck_setup_in_admin),
            fontFamily = PBCFontFamily,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )

        HorizontalDivider(
            modifier = Modifier.padding(2.dp),
            thickness = 2.dp
        )

        Text(
            text = stringResource(Res.string.event_phrase_potluck_setup_in_admin),
            fontFamily = PBCFontFamily,
            fontSize = 16.sp,
            textAlign = TextAlign.Justify,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.event_label_potluck_is_potluck_held),
                fontFamily = PBCFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.weight(1f))

            Checkbox(
                checked = isPotluckChecked,
                onCheckedChange = { newCheckedState ->
                    isPotluckChecked = newCheckedState
                    viewModel.updatePotluckAvailabilityFlag(newCheckedState)
                }
            )
        }

        if (isPotluckChecked) {
            AppButtonWithIcon (
                modifier = Modifier.padding(top = 4.dp),
                label = stringResource(Res.string.event_label_add_to_potluck),
                icon = painterResource(Res.drawable.event_icon_plus)
            ) {
                showCreatePotluckItemDialog.value = true
            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.background)
            ) {
                potluckItemList.forEachIndexed { index, potluckItem ->
                    key (potluckItem.itemId) {
                        PotluckListItem(
                            potluckItem = potluckItem,
                            onDelete = {
                                viewModel.removePotluckItem(potluckItem)
                            }
                        )
                        if (index < potluckItemList.lastIndex) {
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

        PotluckItemCreateDialog(
            showDialog = showCreatePotluckItemDialog,
            onCreate = { potluckItem ->
                viewModel.addPotluckItem(potluckItem)
                showCreatePotluckItemDialog.value = false
            },
            onCancel = {
                showCreatePotluckItemDialog.value = false
            }
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun EventSignUpSheetSetup(viewModel: EventCreateViewModel) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current
    val createOrModifyEvent by viewModel.createOrModifyEvent.collectAsState()
    val signUpSheetItemList by viewModel.signUpSheetItemList.collectAsState()

    var isAdditionalSignUpsChecked by remember { mutableStateOf(createOrModifyEvent.signUpSheetAvailable) }
    val showCreateSignUpSheetItemDialog = remember { mutableStateOf(false) }

    LaunchedEffect(createOrModifyEvent) {
        isAdditionalSignUpsChecked = createOrModifyEvent.signUpSheetAvailable
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
    ) {
        Text(
            text = stringResource(Res.string.event_label_additional_sign_up_sheets),
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
            text = stringResource(Res.string.event_phrase_additional_sign_up_sheets),
            fontFamily = PBCFontFamily,
            fontSize = 16.sp,
            textAlign = TextAlign.Justify,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.event_label_is_additional_sign_ups_available),
                fontFamily = PBCFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.weight(1f))

            Checkbox(
                checked = isAdditionalSignUpsChecked,
                onCheckedChange = { newCheckedState ->
                    isAdditionalSignUpsChecked = newCheckedState
                    viewModel.updateSignUpAvailabilityFlag(newCheckedState)
                }
            )
        }

        if (isAdditionalSignUpsChecked) {
            AppButtonWithIcon (
                modifier = Modifier.padding(top = 8.dp),
                label = stringResource(Res.string.event_label_add_new_sign_up_sheet),
                icon = painterResource(Res.drawable.event_icon_plus)
            ) {
                showCreateSignUpSheetItemDialog.value = true
            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.background)
            ) {
                signUpSheetItemList.forEachIndexed { index, signUpSheet ->
                    key (signUpSheet.sheetId) {
                        SignUpSheetListItem(
                            signUpSheet = signUpSheet,
                            onDelete = {
                                viewModel.removeSignUpSheet(signUpSheet)
                            }
                        )
                        if (index < signUpSheetItemList.lastIndex) {
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

        SignUpSheetCreateDialog(
            showDialog = showCreateSignUpSheetItemDialog,
            onCreate = { signUpSheet ->
                viewModel.addSignUpSheet(signUpSheet)
                showCreateSignUpSheetItemDialog.value = false
            },
            onCancel = {
                showCreateSignUpSheetItemDialog.value = false
            }
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}