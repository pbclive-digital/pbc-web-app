package com.kavi.pbc.web.event.ui.selected

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.event.signup.EventSignUpSheet
import com.kavi.pbc.web.event.data.model.EventActionUiState
import com.kavi.pbc.web.event.ui.common.EventPotluckItemUI
import com.kavi.pbc.web.network.session.Session
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.icon_event_add_item
import pbcwebapp.ui_event.generated.resources.icon_event_remove_item
import pbcwebapp.ui_event.generated.resources.label_event_contribute_potluck
import pbcwebapp.ui_event.generated.resources.label_event_register
import pbcwebapp.ui_event.generated.resources.label_event_registering
import pbcwebapp.ui_event.generated.resources.label_event_remaining_seats
import pbcwebapp.ui_event.generated.resources.label_event_sign_out
import pbcwebapp.ui_event.generated.resources.label_event_sign_up
import pbcwebapp.ui_event.generated.resources.label_event_sign_up_sheet_title
import pbcwebapp.ui_event.generated.resources.label_event_unregister
import pbcwebapp.ui_event.generated.resources.label_event_unregistering
import pbcwebapp.ui_event.generated.resources.phrase_event_contribute_potluck
import pbcwebapp.ui_event.generated.resources.phrase_event_registering
import pbcwebapp.ui_event.generated.resources.phrase_event_unregistering

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationSheetUI(sheetState: SheetState, showSheet: MutableState<Boolean>,
                        viewModel: SelectedEventViewModel) {

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    val givenEvent by viewModel.selectedEvent.collectAsState()
    val eventActionUiState by viewModel.eventActionUiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            showSheet.value = false
        },
        containerColor = MaterialTheme.colorScheme.background,
        scrimColor = themeAdditionalColors.shadow.copy(alpha = .5f)
    ) {
        Box (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 20.dp, end = 20.dp, bottom = 40.dp)
                .fillMaxWidth()
        ) {
            if (Session.isLogIn()) {
                Column {
                    Text(
                        text = if (viewModel.isCurrentUserRegistered())
                            stringResource(Res.string.label_event_unregistering)
                        else
                            stringResource(Res.string.label_event_registering),
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

                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = if (viewModel.isCurrentUserRegistered())
                                painterResource(Res.drawable.icon_event_remove_item)
                            else
                                painterResource(Res.drawable.icon_event_add_item),
                            contentDescription = "Provided icon",
                            modifier = Modifier
                                .size(100.dp)
                        )
                    }

                    Text(
                        text = if (viewModel.isCurrentUserRegistered()) {
                            val resourceString = stringResource(Res.string.phrase_event_unregistering)
                            resourceString.replace("%s", givenEvent.name)
                        } else {
                            val resourceString = stringResource(Res.string.phrase_event_registering)
                            resourceString.replace("%s", givenEvent.name)
                        },
                        fontFamily = PBCFontFamily,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    Text(
                        text = stringResource(Res.string.label_event_remaining_seats)
                            .replace("%s", viewModel.remainingSeatCountAvailable().toString()),
                        fontFamily = PBCFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    if (eventActionUiState == EventActionUiState.PENDING) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        AppFilledButton(
                            modifier = Modifier.padding(top = 16.dp),
                            label = if (viewModel.isCurrentUserRegistered())
                                stringResource(Res.string.label_event_unregister)
                            else
                                stringResource(Res.string.label_event_register)
                        ) {
                            if (viewModel.isCurrentUserRegistered())
                                viewModel.unregisterFromEvent()
                            else
                                viewModel.registerToEvent()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PotluckSheetUI(sheetState: SheetState, showSheet: MutableState<Boolean>,
                   viewModel: SelectedEventViewModel) {

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    val eventPotluckData by viewModel.eventPotluckData.collectAsState()

    val potluckItemCount = eventPotluckData.potluckItemList.size

    val lazyColumHeight = if (potluckItemCount <= 3) {
        400.dp
    } else if(potluckItemCount in 4..6) {
        500.dp
    } else {
        600.dp
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            showSheet.value = false
        },
        containerColor = MaterialTheme.colorScheme.background,
        scrimColor = themeAdditionalColors.shadow.copy(alpha = .5f)
    ) {
        Box (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 20.dp, end = 20.dp, bottom = 40.dp)
                .fillMaxWidth()
        ) {
            if (Session.isLogIn()) {
                Column {
                    Text(
                        text = stringResource(Res.string.label_event_contribute_potluck),
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
                        text = stringResource(Res.string.phrase_event_contribute_potluck),
                        fontFamily = PBCFontFamily,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    LazyColumn (
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .height(lazyColumHeight)
                    ) {
                        items(eventPotluckData.potluckItemList) { potluckItem ->
                            EventPotluckItemUI(
                                modifier = Modifier.padding(bottom = 8.dp),
                                viewModel = viewModel,
                                potluckItem = potluckItem,
                                currentUserContributions = viewModel
                                    .checkedCurrentUserContribution(potluckItem = potluckItem)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpSheetBottomSheetUI(sheetState: SheetState,
                             showSheet: MutableState<Boolean>,
                             selectedSignUpSheet: EventSignUpSheet,
                             viewModel: SelectedEventViewModel) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    val eventActionUiState by viewModel.eventActionUiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    if (eventActionUiState != EventActionUiState.PENDING) {
        isLoading = false
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            showSheet.value = false
        },
        containerColor = MaterialTheme.colorScheme.background,
        scrimColor = themeAdditionalColors.shadow.copy(alpha = .5f)
    ) {
        Box (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 20.dp, end = 20.dp, bottom = 40.dp)
                .fillMaxWidth()
        ) {
            if (Session.isLogIn()) {
                val isSignUp = viewModel.isCurrentUserSignUpToSignUpSheet(selectedSignUpSheet.sheetId)
                Column {
                    Text(
                        text = stringResource(Res.string.label_event_sign_up_sheet_title)
                            .replace("%s", selectedSignUpSheet.sheetName),
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

                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = if (isSignUp)
                                painterResource(Res.drawable.icon_event_remove_item)
                            else
                                painterResource(Res.drawable.icon_event_add_item),
                            contentDescription = "Provided icon",
                            modifier = Modifier
                                .size(100.dp)
                        )
                    }

                    Text(
                        text = selectedSignUpSheet.sheetDescription,
                        fontFamily = PBCFontFamily,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    Text(
                        text = stringResource(Res.string.label_event_remaining_seats)
                            .replace("%s", viewModel.remainingSignUpCountInSignUpSheet(selectedSignUpSheet.sheetId).toString()),
                        fontFamily = PBCFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    if (isLoading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        AppFilledButton(
                            modifier = Modifier.padding(top = 16.dp),
                            label = if (isSignUp)
                                stringResource(Res.string.label_event_sign_out) else stringResource(
                                Res.string.label_event_sign_up)) {

                            isLoading = true

                            if (isSignUp)
                                viewModel.signOutFromSheet(selectedSignUpSheet.sheetId) {
                                    isLoading = false
                                }
                            else
                                viewModel.signUpToSheet(selectedSignUpSheet.sheetId) {
                                    isLoading = false
                                }
                        }
                    }
                }
            }
        }
    }
}