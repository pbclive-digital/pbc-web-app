package com.kavi.pbc.web.appointment.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.appointment.data.model.SelectedType
import com.kavi.pbc.web.appointment.ui.common.AppointmentItem
import com.kavi.pbc.web.appointment.ui.common.AppointmentReqItem
import com.kavi.pbc.web.appointment.ui.common.WithComponent
import com.kavi.pbc.web.appointment.ui.create.appointment.AppointmentCreateOrModifyDialog
import com.kavi.pbc.web.appointment.ui.create.request.RequestCreateOrModifyDialog
import com.kavi.pbc.web.common.ui.component.AppMessageDialog
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.data.appointment.Appointment
import com.kavi.pbc.web.data.appointment.AppointmentRequest
import com.kavi.pbc.web.data.appointment.AppointmentStatus
import com.kavi.pbc.web.data.user.User
import com.kavi.pbc.web.network.session.Session
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.AuthContract
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.ui_appointment.generated.resources.Res
import pbcwebapp.ui_appointment.generated.resources.appointment_label_accepted
import pbcwebapp.ui_appointment.generated.resources.appointment_label_create_request
import pbcwebapp.ui_appointment.generated.resources.appointment_label_details
import pbcwebapp.ui_appointment.generated.resources.appointment_label_dismiss
import pbcwebapp.ui_appointment.generated.resources.appointment_label_empty_appointment_list
import pbcwebapp.ui_appointment.generated.resources.appointment_label_empty_appointment_req_list
import pbcwebapp.ui_appointment.generated.resources.appointment_label_empty_selection
import pbcwebapp.ui_appointment.generated.resources.appointment_label_not_eligible_to_create
import pbcwebapp.ui_appointment.generated.resources.appointment_label_request
import pbcwebapp.ui_appointment.generated.resources.appointment_label_with

@Composable
fun WebContent(maxHeight: Dp, viewModel: AppointmentDashboardViewModel) {
    val appointmentReqList by viewModel.appointmentReqList.collectAsState()
    val appointmentList by viewModel.appointmentList.collectAsState()
    val residentMonkList by viewModel.residentMonkList.collectAsState()
    val requestCreateEligibility by viewModel.appointmentReqCreateEligibility.collectAsState()

    var selectedType by remember { mutableStateOf(SelectedType.NONE) }
    var selectedAppointment by remember { mutableStateOf(Appointment(user = User(email = ""))) }
    var selectedAppointmentReq by remember { mutableStateOf(AppointmentRequest(user = User(email = ""))) }

    val showCreateAppointmentReqDialog = remember { mutableStateOf(false) }
    val showCreateAppointmentDialog = remember { mutableStateOf(false) }
    val isRequestConversion = remember { mutableStateOf(false) }
    val convertingRequestId: MutableState<String?> = remember { mutableStateOf(null) }

    val showNotEligibleMessage = remember { mutableStateOf(false) }
    var selectedMonk: User? by remember { mutableStateOf(null) }
    var modifyAppointmentReq: AppointmentRequest? by remember { mutableStateOf(null) }
    var modifyAppointment: Appointment? by remember { mutableStateOf(null) }

    val showAuthInviteDialog = remember { mutableStateOf(false) }

    Row {
        Column(
            modifier = Modifier
                .weight(.2f)
                .height(maxHeight)
                .padding(top = 10.dp, end = 15.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.appointment_label_create_request),
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp,
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                text = stringResource(Res.string.appointment_label_with),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = PBCFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            LazyColumn {
                item {
                    WithComponent {
                        if (Session.isLogIn()) {
                            if (requestCreateEligibility.allowToCreateRequest) {
                                modifyAppointmentReq = null
                                selectedMonk = null
                                showCreateAppointmentReqDialog.value = true
                            } else {
                                showNotEligibleMessage.value = true
                            }
                        } else {
                            showAuthInviteDialog.value = true
                        }
                    }
                }
                items(residentMonkList) { monk ->
                    WithComponent(user = monk) {
                        if (Session.isLogIn()) {
                            if (requestCreateEligibility.allowToCreateRequest) {
                                modifyAppointmentReq = null
                                selectedMonk = monk
                                showCreateAppointmentReqDialog.value = true
                            } else {
                                showNotEligibleMessage.value = true
                            }
                        } else {
                            showAuthInviteDialog.value = true
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(.35f)
                .height(maxHeight)
                .padding(top = 10.dp, end = 15.dp)
        ) {
            LazyColumn {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.appointment_label_request),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = PBCFontFamily,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Start
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(2.dp),
                        thickness = 2.dp
                    )
                }
                if (appointmentReqList.isNotEmpty()) {
                    items(appointmentReqList) { appointmentReq ->
                        AppointmentReqItem(appointmentReq = appointmentReq, onView = {
                            selectedType = SelectedType.APPOINTMENT_REQ
                            selectedAppointmentReq = appointmentReq
                        }, onAccept = {
                            val appointment = Appointment(
                                title = appointmentReq.title,
                                userId = appointmentReq.userId,
                                user = appointmentReq.user,
                                selectedMonkId = appointmentReq.selectedMonkId,
                                selectedMonk = appointmentReq.selectedMonk,
                                reason = appointmentReq.reason,
                                appointmentStatus = AppointmentStatus.ACCEPTED
                            )
                            modifyAppointment = appointment
                            isRequestConversion.value = true
                            convertingRequestId.value = appointmentReq.id
                            showCreateAppointmentDialog.value = true
                        }, onDelete = {
                            viewModel.deleteAppointmentRequest(appointmentReq.id!!)
                        }, onModify = {
                            modifyAppointmentReq = appointmentReq
                            showCreateAppointmentReqDialog.value = true
                        })
                    }
                } else {
                    item {
                        EmptyList(
                            modifier = Modifier.padding(top = 8.dp),
                            message = stringResource(Res.string.appointment_label_empty_appointment_req_list)
                        )
                    }
                }

                item {
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        text = stringResource(Res.string.appointment_label_accepted),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = PBCFontFamily,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Start
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(2.dp),
                        thickness = 2.dp
                    )
                }

                if (appointmentList.isNotEmpty()) {
                    items(appointmentList) { appointment ->
                        AppointmentItem(appointment = appointment, onView = {
                            selectedType = SelectedType.APPOINTMENT
                            selectedAppointment = appointment
                        }, onDelete = {
                            viewModel.deleteAppointment(appointmentId = appointment.id!!)
                        }, onModify = {
                            modifyAppointment = appointment
                            isRequestConversion.value = false
                            convertingRequestId.value = null
                            showCreateAppointmentDialog.value = true
                        })
                    }
                } else {
                    item {
                        EmptyList(
                            modifier = Modifier.padding(top = 8.dp),
                            message = stringResource(Res.string.appointment_label_empty_appointment_list)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(.45f)
                .height(maxHeight)
                .padding(top = 10.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.appointment_label_details),
                fontFamily = PBCFontFamily,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start
            )

            HorizontalDivider(
                modifier = Modifier.padding(2.dp),
                thickness = 2.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (selectedType) {
                SelectedType.NONE -> EmptySelection()
                SelectedType.APPOINTMENT_REQ -> SelectedAppointmentReq(selectedAppointmentReq)
                SelectedType.APPOINTMENT -> SelectedAppointment(selectedAppointment)
            }
        }
    }

    if (showCreateAppointmentReqDialog.value) {
        RequestCreateOrModifyDialog(
            showDialog = showCreateAppointmentReqDialog,
            onCancel = { refreshRequired ->
                showCreateAppointmentReqDialog.value = false

                // Some update happens in appointment requests, therefore refresh-required
                if (refreshRequired) {
                    viewModel.fetchAppointmentRequests()
                    viewModel.checkAppointmentReqCreateEligibility()
                }
            },
            modifyRequest = modifyAppointmentReq,
            selectedMonk = selectedMonk
        )
    }

    if (showCreateAppointmentDialog.value) {
        AppointmentCreateOrModifyDialog(
            showDialog = showCreateAppointmentDialog,
            modifyAppointment = modifyAppointment,
            isConversion = isRequestConversion.value,
            appointmentReqId = convertingRequestId.value,
            onCancel = { refreshRequired ->
                isRequestConversion.value = false
                convertingRequestId.value = null
                showCreateAppointmentDialog.value = false

                // Some update happens in appointment requests, therefore refresh-required
                if (refreshRequired) {
                    viewModel.fetchAppointmentRequests()
                    viewModel.fetchAppointments()
                    viewModel.checkAppointmentReqCreateEligibility()
                }
            }
        )
    }

    if (showNotEligibleMessage.value) {
        AppMessageDialog(
            showDialog = showNotEligibleMessage,
            message = stringResource(Res.string.appointment_label_not_eligible_to_create),
            actionButtonText = stringResource(Res.string.appointment_label_dismiss)
        ) {
            showNotEligibleMessage.value = false
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
}

@Composable
private fun EmptyList(
    modifier: Modifier = Modifier,
    message: String
) {
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

@Composable
private fun EmptySelection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.appointment_label_empty_selection),
                fontFamily = PBCFontFamily,
                fontSize = 22.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SelectedAppointment(selectedAppointment: Appointment) {
    BoxWithConstraints (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        val maxWidth = this.maxWidth
        if (maxWidth <= 520.dp) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SelectedUserOrMonkImage(selectedAppointment)

                SelectedAppointmentContent(selectedAppointment)
            }
        } else {
            Row {
                Column(modifier = Modifier.weight(.25f)) {
                    SelectedUserOrMonkImage(selectedAppointment)
                }
                Column(
                    modifier = Modifier
                        .weight(.75f)
                        .padding(start = 8.dp, top = 8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    SelectedAppointmentContent(selectedAppointment)
                }
            }
        }
    }
}

@Composable
fun SelectedAppointmentReq(selectedAppointmentRequest: AppointmentRequest) {
    BoxWithConstraints (
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        val maxWidth = this.maxWidth
        if (maxWidth <= 520.dp) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SelectedUserOrMonkImage(selectedAppointmentRequest)

                SelectedAppointmentReqContent(selectedAppointmentRequest)
            }
        } else {
            Row {
                Column (modifier = Modifier.weight(.25f)) {
                    SelectedUserOrMonkImage(selectedAppointmentRequest)
                }
                Column (
                    modifier = Modifier
                        .weight(.75f)
                        .padding(start = 8.dp, top = 8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    SelectedAppointmentReqContent(selectedAppointmentRequest)
                }
            }
        }
    }
}

@Composable
private fun SelectedUserOrMonkImage(selectedAppointment: Appointment) {
    Session.user?.let { currentUser ->
        if (currentUser.residentMonk) {
            WithComponent(user = selectedAppointment.user)
        } else {
            selectedAppointment.selectedMonk?.let {
                WithComponent(user = it)
            }?: run {
                WithComponent()
            }
        }
    }?: run {
        selectedAppointment.selectedMonk?.let {
            WithComponent(user = it)
        }?: run {
            WithComponent()
        }
    }
}

@Composable
private fun SelectedUserOrMonkImage(selectedAppointment: AppointmentRequest) {
    Session.user?.let { currentUser ->
        if (currentUser.residentMonk) {
            WithComponent(user = selectedAppointment.user)
        } else {
            selectedAppointment.selectedMonk?.let {
                WithComponent(user = it)
            }?: run {
                WithComponent()
            }
        }
    }?: run {
        selectedAppointment.selectedMonk?.let {
            WithComponent(user = it)
        }?: run {
            WithComponent()
        }
    }
}

@Composable
private fun SelectedAppointmentContent(selectedAppointment: Appointment) {
    Column {
        Text(
            text = selectedAppointment.title,
            fontFamily = PBCFontFamily,
            fontSize = 28.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = selectedAppointment.reason,
            fontFamily = PBCFontFamily,
            fontSize = 18.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "on ${selectedAppointment.getFormatDate()} at ${selectedAppointment.time}",
            fontFamily = PBCFontFamily,
            fontSize = 18.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun SelectedAppointmentReqContent(selectedAppointmentRequest: AppointmentRequest) {
    Column {
        Text(
            text = selectedAppointmentRequest.title,
            fontFamily = PBCFontFamily,
            fontSize = 28.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = selectedAppointmentRequest.reason,
            fontFamily = PBCFontFamily,
            fontSize = 18.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}