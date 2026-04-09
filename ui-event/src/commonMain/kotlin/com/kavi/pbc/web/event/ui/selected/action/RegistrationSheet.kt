package com.kavi.pbc.web.event.ui.selected.action

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.event.Event
import com.kavi.pbc.web.event.data.model.EventActionUiState
import com.kavi.pbc.web.event.data.model.RegUnRegType
import com.kavi.pbc.web.event.ui.selected.SelectedEventViewModel
import com.kavi.pbc.web.network.session.Session
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_event.generated.resources.Res
import pbcwebapp.ui_event.generated.resources.event_icon_add_item
import pbcwebapp.ui_event.generated.resources.event_icon_process_failed
import pbcwebapp.ui_event.generated.resources.event_icon_register_success
import pbcwebapp.ui_event.generated.resources.event_icon_remove_item
import pbcwebapp.ui_event.generated.resources.event_icon_unregister_success
import pbcwebapp.ui_event.generated.resources.event_label_close
import pbcwebapp.ui_event.generated.resources.event_label_reg_success
import pbcwebapp.ui_event.generated.resources.event_label_reg_un_reg_failure
import pbcwebapp.ui_event.generated.resources.event_label_register
import pbcwebapp.ui_event.generated.resources.event_label_registering
import pbcwebapp.ui_event.generated.resources.event_label_remaining_seats
import pbcwebapp.ui_event.generated.resources.event_label_un_reg_success
import pbcwebapp.ui_event.generated.resources.event_label_unregister
import pbcwebapp.ui_event.generated.resources.event_label_unregistering
import pbcwebapp.ui_event.generated.resources.event_phrase_reg_success
import pbcwebapp.ui_event.generated.resources.event_phrase_reg_un_reg_failure
import pbcwebapp.ui_event.generated.resources.event_phrase_registering
import pbcwebapp.ui_event.generated.resources.event_phrase_un_reg_success
import pbcwebapp.ui_event.generated.resources.event_phrase_unregistering

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationSheetUI(sheetState: SheetState, showSheet: MutableState<Boolean>,
                        viewModel: SelectedEventViewModel) {

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    val givenEvent by viewModel.selectedEvent.collectAsState()
    val eventRegUnRegUiState by viewModel.eventRegUnRegUiState.collectAsState()
    val isCurrentUserRegistered = viewModel.isCurrentUserRegistered()

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
                when(eventRegUnRegUiState) {
                    EventActionUiState.INITIAL -> {
                        RegUnRegInitialUI(viewModel = viewModel, isCurrentUserRegistered = isCurrentUserRegistered, givenEvent = givenEvent)
                    }
                    EventActionUiState.PENDING -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    EventActionUiState.FAILURE -> {
                        RegUnRegFailure(viewModel = viewModel, showSheet = showSheet)
                    }
                    EventActionUiState.SUCCESS -> {
                        val regUnRegType = if (isCurrentUserRegistered) RegUnRegType.UNREGISTER else RegUnRegType.REGISTER
                        RegUnRegSuccess(viewModel = viewModel, regUnRegType = regUnRegType, showSheet = showSheet)
                    }
                }
            }
        }
    }
}

@Composable
fun RegUnRegInitialUI(viewModel: SelectedEventViewModel, isCurrentUserRegistered: Boolean, givenEvent: Event) {
    Column {
        Text(
            text = if (isCurrentUserRegistered)
                stringResource(Res.string.event_label_unregistering)
            else
                stringResource(Res.string.event_label_registering),
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

        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = if (isCurrentUserRegistered)
                    painterResource(Res.drawable.event_icon_remove_item)
                else
                    painterResource(Res.drawable.event_icon_add_item),
                contentDescription = "Provided icon",
                modifier = Modifier
                    .size(100.dp)
            )
        }

        Text(
            text = if (isCurrentUserRegistered) {
                val resourceString = stringResource(Res.string.event_phrase_unregistering)
                resourceString.replace("%s", givenEvent.name)
            } else {
                val resourceString = stringResource(Res.string.event_phrase_registering)
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
            text = stringResource(Res.string.event_label_remaining_seats)
                .replace("%s", viewModel.remainingSeatCountAvailable().toString()),
            fontFamily = PBCFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        AppFilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = if (viewModel.isCurrentUserRegistered())
                stringResource(Res.string.event_label_unregister)
            else
                stringResource(Res.string.event_label_register)
        ) {
            if (viewModel.isCurrentUserRegistered())
                viewModel.unregisterFromEvent()
            else
                viewModel.registerToEvent()
        }
    }
}

@Composable
fun RegUnRegSuccess(viewModel: SelectedEventViewModel, regUnRegType: RegUnRegType, showSheet: MutableState<Boolean>) {
    Column {
        Text(
            text = when(regUnRegType){
                RegUnRegType.REGISTER -> stringResource(Res.string.event_label_reg_success)
                RegUnRegType.UNREGISTER -> stringResource(Res.string.event_label_un_reg_success)
            },
            fontFamily = PBCFontFamily,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = when(regUnRegType) {
                    RegUnRegType.REGISTER -> painterResource(Res.drawable.event_icon_register_success)
                    RegUnRegType.UNREGISTER -> painterResource(Res.drawable.event_icon_unregister_success)
                },
                contentDescription = "Provided icon",
                modifier = Modifier
                    .size(100.dp)
            )
        }

        Text(
            text = when(regUnRegType){
                RegUnRegType.REGISTER -> stringResource(Res.string.event_phrase_reg_success)
                RegUnRegType.UNREGISTER -> stringResource(Res.string.event_phrase_un_reg_success)
            },
            fontFamily = PBCFontFamily,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        AppFilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(Res.string.event_label_close)
        ) {
            showSheet.value = false
            viewModel.revokeRegUnRegUiState()
        }
    }
}

@Composable
fun RegUnRegFailure(viewModel: SelectedEventViewModel, showSheet: MutableState<Boolean>) {
    Column {
        Text(
            text = stringResource(Res.string.event_label_reg_un_reg_failure),
            fontFamily = PBCFontFamily,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.event_icon_process_failed),
                contentDescription = "Provided icon",
                modifier = Modifier
                    .size(100.dp)
            )
        }

        Text(
            text = stringResource(Res.string.event_phrase_reg_un_reg_failure),
            fontFamily = PBCFontFamily,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        AppFilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = stringResource(Res.string.event_label_close)
        ) {
            showSheet.value = false
            viewModel.revokeRegUnRegUiState()
        }
    }
}