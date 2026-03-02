package com.kavi.pbc.web.event.ui.selected

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kavi.pbc.web.common.ui.component.AppButtonWithIcon
import com.kavi.pbc.web.common.ui.component.AppFullScreenLoader
import com.kavi.pbc.web.common.ui.component.AppIconButton
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.data.event.EventStatus
import com.kavi.pbc.web.data.event.VenueType
import com.kavi.pbc.web.data.event.signup.EventSignUpSheet
import com.kavi.pbc.web.event.data.model.EventActionUiState
import com.kavi.pbc.web.event.ui.common.SignUpSheetItemUI
import com.kavi.pbc.web.event.ui.selected.action.PotluckSheetUI
import com.kavi.pbc.web.event.ui.selected.action.RegistrationSheetUI
import com.kavi.pbc.web.event.ui.selected.action.SignUpSheetBottomSheetUI
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import com.kavi.pbc.web.parent.extention.openUrl
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_icon_location
import pbcwebapp.ui_event.generated.resources.event_icon_online_meeting
import pbcwebapp.ui_event.generated.resources.event_image_pbc
import pbcwebapp.ui_event.generated.resources.event_icon_potluck_register
import pbcwebapp.ui_event.generated.resources.event_icon_register
import pbcwebapp.ui_event.generated.resources.event_label_additional_sign_ups
import pbcwebapp.ui_event.generated.resources.event_label_at
import pbcwebapp.ui_event.generated.resources.event_label_from
import pbcwebapp.ui_event.generated.resources.event_label_on
import pbcwebapp.ui_event.generated.resources.event_label_potluck
import pbcwebapp.ui_event.generated.resources.event_label_potluck_details
import pbcwebapp.ui_event.generated.resources.event_label_register_or_unregister
import pbcwebapp.ui_event.generated.resources.event_label_registration
import pbcwebapp.ui_event.generated.resources.event_phrase_additional_sign_ups
import pbcwebapp.ui_event.generated.resources.event_phrase_potluck_details
import pbcwebapp.ui_event.generated.resources.event_phrase_registration_details

@Composable
fun SelectedEvent(navController: NavController, eventId: String) {

    val viewModel: SelectedEventViewModel = viewModel { SelectedEventViewModel() }

    var screenType by remember { mutableStateOf(ScreenType.COMPUTER) }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val maxWidth = this.maxWidth

        LaunchedEffect(Unit) {
            viewModel.fetchEventDetails(eventId = eventId)
            screenType = UIUtil.screenType(maxWidth)
        }

        when(screenType) {
            ScreenType.PHONE -> PhoneScreenUI(viewModel = viewModel)
            else -> WebScreenUI(viewModel = viewModel)
        }
    }
}

@Composable
fun PhoneScreenUI(viewModel: SelectedEventViewModel) {

    val fetchSelectedEventState by viewModel.fetchSelectedEventState.collectAsState()
    val selectedEvent by viewModel.selectedEvent.collectAsState()

    when(fetchSelectedEventState) {
        EventActionUiState.INITIAL -> {}
        EventActionUiState.FAILURE -> {}
        EventActionUiState.PENDING -> {
            AppFullScreenLoader()
        }
        EventActionUiState.SUCCESS -> {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                BoxWithConstraints (
                    contentAlignment = Alignment.Center
                ) {
                    val maxWidth = this.maxWidth

                    Card (
                        modifier = Modifier
                            .height(maxWidth)
                            .width(maxWidth)
                            .padding(10.dp)
                            .background(Color.Transparent),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                    ) {
                        AsyncImage(
                            model = selectedEvent.eventImage,
                            error = painterResource(Res.drawable.event_image_pbc),
                            contentDescription = null, // decorative image
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .fillMaxSize()
                                //.padding(20.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(color = MaterialTheme.colorScheme.background)
                        )
                    }
                }

                EventInformationComponent(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebScreenUI(viewModel: SelectedEventViewModel) {

    val fetchSelectedEventState by viewModel.fetchSelectedEventState.collectAsState()
    val selectedEvent by viewModel.selectedEvent.collectAsState()

    when(fetchSelectedEventState) {
        EventActionUiState.INITIAL -> {}
        EventActionUiState.FAILURE -> {}
        EventActionUiState.PENDING -> {
            AppFullScreenLoader()
        }
        EventActionUiState.SUCCESS -> {
            Row (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                BoxWithConstraints (
                    modifier = Modifier
                        .weight(.5f)
                ) {
                    val maxWidth = this.maxWidth

                    Card (
                        modifier = Modifier
                            .height(maxWidth)
                            .width(maxWidth)
                            .padding(top = 20.dp, bottom = 20.dp, end = 20.dp)
                            .background(Color.Transparent),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                    ) {
                        AsyncImage(
                            model = selectedEvent.eventImage,
                            error = painterResource(Res.drawable.event_image_pbc),
                            contentDescription = null, // decorative image
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(color = MaterialTheme.colorScheme.background)
                        )
                    }
                }

                EventInformationComponent(
                    modifier = Modifier
                        .weight(.5f)
                        .verticalScroll(rememberScrollState()),
                    viewModel = viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventInformationComponent(modifier: Modifier = Modifier, viewModel: SelectedEventViewModel) {
    val selectedEvent by viewModel.selectedEvent.collectAsState()
    val eventSignUpSheetData by viewModel.eventSignUpSheetData.collectAsState()

    val showAuthInviteDialog = remember { mutableStateOf(false) }

    val registrationSheetState = rememberModalBottomSheetState()
    val showRegistrationSheet = remember { mutableStateOf(false) }

    val potluckSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val showPotluckSheet = remember { mutableStateOf(false) }

    val signUpSheetBottomSheetState = rememberModalBottomSheetState()
    val showSignUpSheetBottomSheet = remember { mutableStateOf(false) }
    val selectedSignUpSheetItem = remember { mutableStateOf(EventSignUpSheet()) }

    Column (
        modifier = modifier
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Text(
            text = selectedEvent.name,
            fontFamily = PBCFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 40.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = selectedEvent.description,
            fontFamily = PBCFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row (
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(Res.string.event_label_on),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = selectedEvent.getFormatDate(),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(Res.string.event_label_from),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = "${selectedEvent.startTime} - ${selectedEvent.endTime}",
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Row (
            modifier = Modifier.padding(top = 12.dp),
        ) {
            if (selectedEvent.venueType == VenueType.PHYSICAL) {
                Text(
                    text = stringResource(Res.string.event_label_at),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = selectedEvent.getPlace(),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.weight(1f))

                AppIconButton(
                    icon = painterResource(Res.drawable.event_icon_location),
                    buttonSize = 40.dp
                ) {
                    selectedEvent.venueAddress?.let {
                        val addressUrl = it.replace(" ", "+")
                        val locationUrl = "https://www.google.com/maps/place/$addressUrl"
                        openUrl(url = locationUrl)
                    }
                }
            } else {
                Text(
                    text = selectedEvent.getPlace(),
                    fontFamily = PBCFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.weight(1f))

                AppIconButton(
                    icon = painterResource(Res.drawable.event_icon_online_meeting),
                    buttonSize = 40.dp
                ) {
                    selectedEvent.meetingUrl?.let {
                        openUrl(url = it)
                    }
                }
            }
        }

        if (selectedEvent.registrationRequired && selectedEvent.eventStatus == EventStatus.PUBLISHED) {

            var registrationBottomPadding = 0.dp
            if (!selectedEvent.potluckAvailable) {
                registrationBottomPadding = 40.dp
            }

            Column(
                modifier = Modifier.padding(top = 16.dp, bottom = registrationBottomPadding)
            ) {
                Text(
                    text = stringResource(Res.string.event_label_registration),
                    fontFamily = PBCFontFamily,
                    fontSize = 22.sp,
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
                    text = stringResource(Res.string.event_phrase_registration_details),
                    fontFamily = PBCFontFamily,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                AppButtonWithIcon(
                    modifier = Modifier.padding(top = 8.dp),
                    label = stringResource(Res.string.event_label_register_or_unregister),
                    icon = painterResource(Res.drawable.event_icon_register)
                ) {
                    if (Session.isLogIn())
                        showRegistrationSheet.value = true
                    else
                        showAuthInviteDialog.value = true
                }
            }
        }

        if (selectedEvent.potluckAvailable && selectedEvent.eventStatus == EventStatus.PUBLISHED) {
            var potluckBottomPadding = 0.dp
            if (!selectedEvent.signUpSheetAvailable) {
                potluckBottomPadding = 40.dp
            }

            Column(
                modifier = Modifier.padding(top = 20.dp, bottom = potluckBottomPadding)
            ) {
                Text(
                    text = stringResource(Res.string.event_label_potluck),
                    fontFamily = PBCFontFamily,
                    fontSize = 22.sp,
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
                    text = stringResource(Res.string.event_phrase_potluck_details),
                    fontFamily = PBCFontFamily,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                AppButtonWithIcon(
                    modifier = Modifier.padding(top = 8.dp),
                    label = stringResource(Res.string.event_label_potluck_details),
                    icon = painterResource(Res.drawable.event_icon_potluck_register)
                ) {
                    if (Session.isLogIn())
                        showPotluckSheet.value = true
                    else
                        showAuthInviteDialog.value = true
                }
            }
        }

        if (selectedEvent.signUpSheetAvailable && selectedEvent.eventStatus == EventStatus.PUBLISHED) {
            Column(
                modifier = Modifier.padding(top = 20.dp, bottom = 40.dp)
            ) {
                Text(
                    text = stringResource(Res.string.event_label_additional_sign_ups),
                    fontFamily = PBCFontFamily,
                    fontSize = 22.sp,
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
                    text = stringResource(Res.string.event_phrase_additional_sign_ups),
                    fontFamily = PBCFontFamily,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                eventSignUpSheetData.signUpSheetItemList.forEach { signUpSheetItem ->
                    Spacer(modifier = Modifier.height(8.dp))
                    SignUpSheetItemUI(
                        modifier = Modifier
                            .clickable {
                                // Open up bottom sheet to register to selected sign-up sheet
                                if (Session.isLogIn()) {
                                    selectedSignUpSheetItem.value = signUpSheetItem
                                    showSignUpSheetBottomSheet.value = true
                                } else
                                    showAuthInviteDialog.value = true
                            },
                        signUpSheet = signUpSheetItem
                    )
                }
            }
        }
    }

    if (showAuthInviteDialog.value) {
        ContractServiceLocator.locate(AuthContract::class).ProvideSignUpInviteUI(
            showDialog = showAuthInviteDialog,
            onCancel = {
                showAuthInviteDialog.value = false
            }
        )
    }

    if (showRegistrationSheet.value) {
        RegistrationSheetUI(sheetState = registrationSheetState, showSheet = showRegistrationSheet, viewModel = viewModel)
    }

    if (showPotluckSheet.value) {
        PotluckSheetUI(
            sheetState = potluckSheetState,
            showSheet = showPotluckSheet,
            viewModel = viewModel
        )
    }

    if (showSignUpSheetBottomSheet.value) {
        SignUpSheetBottomSheetUI(
            sheetState = signUpSheetBottomSheetState,
            showSheet = showSignUpSheetBottomSheet,
            selectedSignUpSheet = selectedSignUpSheetItem.value,
            viewModel = viewModel
        )
    }
}