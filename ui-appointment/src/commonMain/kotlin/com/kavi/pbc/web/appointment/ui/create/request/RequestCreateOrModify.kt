package com.kavi.pbc.web.appointment.ui.create.request

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.kavi.pbc.web.common.ui.component.AppDropDownMenu
import com.kavi.pbc.web.common.ui.component.AppFilledButton
import com.kavi.pbc.web.common.ui.component.AppOutlineMultiLineTextField
import com.kavi.pbc.web.common.ui.component.AppOutlineTextField
import com.kavi.pbc.web.common.ui.component.ErrorMessageBalloon
import com.kavi.pbc.web.common.ui.component.SuccessMessageBalloon
import com.kavi.pbc.web.common.ui.component.TitleWithAction
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import com.kavi.pbc.web.data.appointment.AppointmentRequest
import com.kavi.pbc.web.data.appointment.AppointmentRequestType
import com.kavi.pbc.web.data.user.User
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_appointment.generated.resources.Res
import pbcwebapp.ui_appointment.generated.resources.appointment_icon_close_x
import pbcwebapp.ui_appointment.generated.resources.appointment_label_how
import pbcwebapp.ui_appointment.generated.resources.appointment_label_new_request
import pbcwebapp.ui_appointment.generated.resources.appointment_label_reason
import pbcwebapp.ui_appointment.generated.resources.appointment_label_request_create
import pbcwebapp.ui_appointment.generated.resources.appointment_label_request_modify
import pbcwebapp.ui_appointment.generated.resources.appointment_label_select
import pbcwebapp.ui_appointment.generated.resources.appointment_label_select_monk
import pbcwebapp.ui_appointment.generated.resources.appointment_label_title
import pbcwebapp.ui_appointment.generated.resources.appointment_label_why
import pbcwebapp.ui_appointment.generated.resources.appointment_label_with
import pbcwebapp.ui_appointment.generated.resources.appointment_pharse_with
import pbcwebapp.ui_appointment.generated.resources.appointment_phrase_how
import pbcwebapp.ui_appointment.generated.resources.appointment_phrase_request_create_error
import pbcwebapp.ui_appointment.generated.resources.appointment_phrase_request_create_or_modify_empty_fields
import pbcwebapp.ui_appointment.generated.resources.appointment_phrase_request_create_success
import pbcwebapp.ui_appointment.generated.resources.appointment_phrase_why

@Composable
fun RequestCreateOrModifyDialog(
    showDialog: MutableState<Boolean>,
    modifyRequest: AppointmentRequest? = null,
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
        NewAppointmentRequestUI(modifyRequest = modifyRequest, selectedMonk = selectedMonk, onCancel = onCancel)
    }
}

@Composable
private fun NewAppointmentRequestUI(
    modifyRequest: AppointmentRequest?,
    selectedMonk: User? = null,
    onCancel: (refreshRequired: Boolean) -> Unit
) {
    val viewModel: RequestCreateOrModifyViewModel = viewModel { RequestCreateOrModifyViewModel() }
    var isModify by remember { mutableStateOf(false) }

    modifyRequest?.let {
        isModify = true
        viewModel.setModifyAppointmentReq(appointmentReq = it)
    }?: run {
        viewModel.initiateNewAppointmentReq(selectedMonk = selectedMonk)
    }

    var anyModifySuccess by remember { mutableStateOf(false) }

    val createOrModifyAppointmentReq by viewModel.createOrModifyAppointmentReq.collectAsState()
    val residenceMonkList by viewModel.residenceMonkList.collectAsState()
    val appointmentReqUiStatus by viewModel.appointmentReqUiStatus.collectAsState()

    val appointmentTitle = remember { mutableStateOf(TextFieldValue(createOrModifyAppointmentReq?.title?: "")) }
    val appointmentWith = remember { mutableStateOf(viewModel.getInitialSelectedMonk()) }
    val appointmentType = remember { mutableStateOf(createOrModifyAppointmentReq?.appointmentReqType?.name?: "" ) }
    val appointmentReason = remember { mutableStateOf(TextFieldValue(createOrModifyAppointmentReq?.reason?: "")) }

    val errorBalloonVisibility = remember { mutableStateOf(false) }
    var errorBalloonMessage by remember { mutableStateOf("") }
    val successBalloonVisibility = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getResidentMonkList()
    }

    LaunchedEffect(appointmentWith.value) {
        viewModel.updateSelectedMonk(selectedMonkName = appointmentWith.value)
    }

    LaunchedEffect(appointmentType.value) {
        viewModel.updateAppointmentType(appointmentType = appointmentType.value)
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
                        titleText = stringResource(Res.string.appointment_label_new_request),
                        textSize = 28,
                        actionPainter = painterResource(Res.drawable.appointment_icon_close_x),
                        actionPainterSize = 25.dp,
                        isIcon = true,
                    ) {
                        viewModel.clearAppointmentRequest()
                        errorBalloonVisibility.value = false
                        viewModel.revokeAppointmentReqUiStatus()
                        onCancel.invoke(anyModifySuccess)
                    }
                }
                else -> {
                    TitleWithAction(
                        titleText = stringResource(Res.string.appointment_label_new_request),
                        actionPainter = painterResource(Res.drawable.appointment_icon_close_x),
                        actionPainterSize = 40.dp,
                        isIcon = true,
                    ) {
                        viewModel.clearAppointmentRequest()
                        errorBalloonVisibility.value = false
                        viewModel.revokeAppointmentReqUiStatus()
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
                        viewModel.revokeAppointmentReqUiStatus()
                    }
                )
                SuccessMessageBalloon(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                    showBalloon = successBalloonVisibility,
                    successMessage = stringResource(Res.string.appointment_phrase_request_create_success),
                    onDismiss = {
                        successBalloonVisibility.value = false
                        viewModel.revokeAppointmentReqUiStatus()
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
                text = stringResource(Res.string.appointment_label_how),
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
                text = stringResource(Res.string.appointment_phrase_how),
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
                title = stringResource(Res.string.appointment_label_select),
                selectableItems = listOf(
                    AppointmentRequestType.REMOTE.name, AppointmentRequestType.ON_SITE.name
                ),
                selectedItem = appointmentType,
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
                viewModel.updateTitle(appointmentTitle.value.text)
                viewModel.updateReason(appointmentReason.value.text)

                if(isModify)
                    viewModel.updateAppointmentRequest()
                else
                    viewModel.createNewAppointmentRequest()
            }
        }

        when(appointmentReqUiStatus) {
            AppointmentCreateOrModifyUiStatus.NONE -> {
                //errorBalloonVisibility.value = false
            }
            AppointmentCreateOrModifyUiStatus.PENDING -> {}
            AppointmentCreateOrModifyUiStatus.FAILURE -> {
                errorBalloonMessage = stringResource(Res.string.appointment_phrase_request_create_error)
                errorBalloonVisibility.value = true
            }
            AppointmentCreateOrModifyUiStatus.EMPTY_FIELD -> {
                errorBalloonMessage = stringResource(Res.string.appointment_phrase_request_create_or_modify_empty_fields)
                errorBalloonVisibility.value = true
            }
            AppointmentCreateOrModifyUiStatus.SUCCESS -> {
                anyModifySuccess = true
                successBalloonVisibility.value = true

                // Clear the question form
                appointmentTitle.value = TextFieldValue("")
                appointmentReason.value = TextFieldValue("")
            }
        }
    }
}