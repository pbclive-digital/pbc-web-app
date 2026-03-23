package com.kavi.pbc.web.appointment.ui.create.appointment

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kavi.pbc.web.appointment.data.model.AppointmentCreateOrModifyUiStatus
import com.kavi.pbc.web.common.ui.component.AppBasicDialog
import com.kavi.pbc.web.common.ui.component.AppDatePickerButton
import com.kavi.pbc.web.common.ui.component.AppDatePickerDialog
import com.kavi.pbc.web.common.ui.component.AppDropDownMenu
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppOutlineMultiLineTextField
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.component.AppTimePickerButton
import com.kavi.pbc.web.common.ui.component.AppTimePickerDialog
import com.kavi.pbc.web.common.ui.component.ErrorMessageBalloon
import com.kavi.pbc.web.common.ui.component.SuccessMessageBalloon
import com.kavi.pbc.web.common.ui.component.TitleWithAction
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.data.appointment.Appointment
import com.kavi.pbc.web.data.user.User
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_appointment.generated.resources.Res
import pbcwebapp.ui_appointment.generated.resources.appointment_icon_close_x
import pbcwebapp.ui_appointment.generated.resources.appointment_label_new_appointment
import pbcwebapp.ui_appointment.generated.resources.appointment_label_reason
import pbcwebapp.ui_appointment.generated.resources.appointment_label_request_create
import pbcwebapp.ui_appointment.generated.resources.appointment_label_request_modify
import pbcwebapp.ui_appointment.generated.resources.appointment_label_select_monk
import pbcwebapp.ui_appointment.generated.resources.appointment_label_title
import pbcwebapp.ui_appointment.generated.resources.appointment_label_update_appointment
import pbcwebapp.ui_appointment.generated.resources.appointment_label_when
import pbcwebapp.ui_appointment.generated.resources.appointment_label_why
import pbcwebapp.ui_appointment.generated.resources.appointment_label_with
import pbcwebapp.ui_appointment.generated.resources.appointment_pharse_with
import pbcwebapp.ui_appointment.generated.resources.appointment_phase_devotee_name
import pbcwebapp.ui_appointment.generated.resources.appointment_phase_when
import pbcwebapp.ui_appointment.generated.resources.appointment_phrase_appointment_create_error
import pbcwebapp.ui_appointment.generated.resources.appointment_phrase_appointment_create_success
import pbcwebapp.ui_appointment.generated.resources.appointment_phrase_create_or_modify_empty_fields
import pbcwebapp.ui_appointment.generated.resources.appointment_phrase_why

@Composable
fun AppointmentCreateOrModifyDialog(
    showDialog: MutableState<Boolean>,
    modifyAppointment: Appointment? = null,
    isConversion: Boolean = false,
    appointmentReqId: String? = null,
    selectedMonk: User? = null,
    onCancel: (refreshRequired: Boolean) -> Unit
) {
    AppBasicDialog(
        modifier = Modifier.fillMaxSize(),
        showDialog = showDialog.value,
        onDismissRequest = {
            onCancel.invoke(false)
        }
    ) {
        NewAppointmentUI(
            modifyAppointment = modifyAppointment,
            isConversion = isConversion,
            selectedMonk = selectedMonk,
            appointmentReqId = appointmentReqId,
            onCancel = onCancel
        )
    }
}

@Composable
private fun NewAppointmentUI(
    modifyAppointment: Appointment?,
    isConversion: Boolean = false,
    appointmentReqId: String? = null,
    selectedMonk: User? = null,
    onCancel: (refreshRequired: Boolean) -> Unit
) {
    val viewModel: AppointmentCreateOrModifyViewModel = viewModel { AppointmentCreateOrModifyViewModel() }
    var isModify by remember { mutableStateOf(false) }

    modifyAppointment?.let {
        if (!isConversion)
            isModify = true
        viewModel.setModifyAppointment(appointmentReq = it)
    }?: run {
        viewModel.initiateNewAppointment(selectedMonk = selectedMonk)
    }

    var anyModifySuccess by remember { mutableStateOf(false) }

    val createOrModifyAppointment by viewModel.createOrModifyAppointment.collectAsState()
    val residenceMonkList by viewModel.residenceMonkList.collectAsState()
    val appointmentUiStatus by viewModel.appointmentUiStatus.collectAsState()

    val appointmentTitle = remember { mutableStateOf(TextFieldValue(createOrModifyAppointment?.title?: "")) }
    val appointmentDate = remember { mutableStateOf(viewModel.getInitialAppointmentDate()) }
    val appointmentTime = remember { mutableStateOf(viewModel.getInitialTime()) }
    val appointmentWith = remember { mutableStateOf(viewModel.getInitialSelectedMonk()) }
    val appointmentReason = remember { mutableStateOf(TextFieldValue(createOrModifyAppointment?.reason?: "")) }

    val errorBalloonVisibility = remember { mutableStateOf(false) }
    var errorBalloonMessage by remember { mutableStateOf("") }
    val successBalloonVisibility = remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val showDatePicker = remember { mutableStateOf(false) }

    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getResidentMonkList()
    }

    LaunchedEffect(appointmentWith.value) {
        viewModel.updateSelectedMonk(selectedMonkName = appointmentWith.value)
    }

    BoxWithConstraints(
        contentAlignment = Alignment.Center
    ) {
        val maxWidth = this.maxWidth
        val screenType = UIUtil.screenType(maxWidth = maxWidth)

        Column (
            modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when(screenType) {
                ScreenType.PHONE -> {
                    TitleWithAction(
                        titleText = if (isModify)
                            stringResource(Res.string.appointment_label_update_appointment)
                        else
                            stringResource(Res.string.appointment_label_new_appointment),
                        textSize = 28,
                        actionPainter = painterResource(Res.drawable.appointment_icon_close_x),
                        actionPainterSize = 25.dp,
                        isIcon = true,
                    ) {
                        viewModel.clearAppointment()
                        errorBalloonVisibility.value = false
                        viewModel.revokeAppointmentUiStatus()
                        onCancel.invoke(anyModifySuccess)
                    }
                }
                else -> {
                    TitleWithAction(
                        titleText = if (isModify)
                            stringResource(Res.string.appointment_label_update_appointment)
                        else
                            stringResource(Res.string.appointment_label_new_appointment),
                        actionPainter = painterResource(Res.drawable.appointment_icon_close_x),
                        actionPainterSize = 40.dp,
                        isIcon = true,
                    ) {
                        viewModel.clearAppointment()
                        errorBalloonVisibility.value = false
                        viewModel.revokeAppointmentUiStatus()
                        onCancel.invoke(anyModifySuccess)
                    }
                }
            }

            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                // Error or Success message balloon
                ErrorMessageBalloon(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                    showBalloon = errorBalloonVisibility,
                    errorMessage = errorBalloonMessage,
                    onDismiss = {
                        errorBalloonVisibility.value = false
                        viewModel.revokeAppointmentUiStatus()
                    }
                )
                SuccessMessageBalloon(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                    showBalloon = successBalloonVisibility,
                    successMessage = stringResource(Res.string.appointment_phrase_appointment_create_success),
                    onDismiss = {
                        successBalloonVisibility.value = false
                        viewModel.revokeAppointmentUiStatus()
                    }
                )
            }

            AppOutlineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                headingText = stringResource(Res.string.appointment_label_title).uppercase(),
                contentText = appointmentTitle,
                onValueChange = { newValue ->
                    appointmentTitle.value = newValue
                    viewModel.updateTitle(appointmentTitle.value.text)
                }
            )

            Text(
                text = stringResource(Res.string.appointment_label_when),
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

            Text(
                text = stringResource(Res.string.appointment_phase_when),
                fontFamily = PBCFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 4.dp, end = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppDatePickerButton (
                    modifier = Modifier.width(200.dp),
                    label = appointmentDate
                ) {
                    showDatePicker.value = true
                }

                Spacer(modifier = Modifier.weight(1f))

                AppTimePickerButton(
                    modifier = Modifier
                        .width(150.dp),
                    label = appointmentTime
                ) {
                    showTimePicker = true
                }
            }

            Text(
                text = stringResource(Res.string.appointment_label_with),
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

            Text(
                text = stringResource(Res.string.appointment_pharse_with),
                fontFamily = PBCFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )

            AppDropDownMenu(
                modifier = Modifier
                    .padding(top = 4.dp),
                title = stringResource(Res.string.appointment_label_select_monk),
                selectableItems = residenceMonkList,
                selectedItem = appointmentWith,
            )

            Text(
                text = stringResource(Res.string.appointment_phase_devotee_name),
                fontFamily = PBCFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )

            Text(
                text = "${createOrModifyAppointment?.user?.firstName} ${createOrModifyAppointment?.user?.lastName} (${createOrModifyAppointment?.user?.email})",
                fontFamily = PBCFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )

            Text(
                text = stringResource(Res.string.appointment_label_why),
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

            Text(
                text = stringResource(Res.string.appointment_phrase_why),
                fontFamily = PBCFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )

            AppOutlineMultiLineTextField (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .height(120.dp),
                headingText = stringResource(Res.string.appointment_label_reason),
                contentText = appointmentReason,
                maxLines = 6,
                onValueChange = { newValue ->
                    appointmentReason.value = newValue
                    viewModel.updateReason(appointmentReason.value.text)
                }
            )

            AppFilledButton(
                label = if (isModify) stringResource(Res.string.appointment_label_request_modify)
                else stringResource(Res.string.appointment_label_request_create),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                if(isModify)
                    viewModel.updateAppointment()
                else
                    viewModel.createNewAppointment(appointmentReqId = appointmentReqId)

            }
        }

        when(appointmentUiStatus) {
            AppointmentCreateOrModifyUiStatus.NONE -> {
                //errorBalloonVisibility.value = false
            }
            AppointmentCreateOrModifyUiStatus.PENDING -> {}
            AppointmentCreateOrModifyUiStatus.FAILURE -> {
                errorBalloonMessage = stringResource(Res.string.appointment_phrase_appointment_create_error)
                errorBalloonVisibility.value = true
            }
            AppointmentCreateOrModifyUiStatus.EMPTY_FIELD -> {
                errorBalloonMessage = stringResource(Res.string.appointment_phrase_create_or_modify_empty_fields)
                errorBalloonVisibility.value = true
            }
            AppointmentCreateOrModifyUiStatus.SUCCESS -> {
                anyModifySuccess = true
                successBalloonVisibility.value = true

                // Clear the question form
                appointmentTitle.value = TextFieldValue("")
                appointmentReason.value = TextFieldValue("")
                appointmentDate.value = viewModel.getInitialAppointmentDate()
                appointmentTime.value = viewModel.getInitialTime()
            }
        }
    }

    if (showDatePicker.value) {
        AppDatePickerDialog (
            showDatePicker = showDatePicker,
            datePickerState = datePickerState,
            onConfirmAction = {
                showDatePicker.value = false
                appointmentDate.value = viewModel.formatDate(datePickerState.selectedDateMillis)
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
                appointmentTime.value = viewModel.formatTime(hour = hour, minute = minute)
                viewModel.updateTime(appointmentTime.value)
            },
            onDismiss = { showTimePicker = false }
        )
    }
}